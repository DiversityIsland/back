package com.amr.project.webapp.handler;

import com.amr.project.model.entity.Order;
import com.amr.project.model.enums.PaymentStatus;
import com.amr.project.service.abstracts.OrderService;
import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.CreateBillInfo;
import com.qiwi.billpayments.sdk.model.in.Customer;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.UUID;

@Component
public class PaymentApi {

    private final OrderService orderService;

//    String secretKey = "eyJ2ZXJzaW9uIjoicmVzdF92MyIsImRhdGEiOnsibWVyY2hhbnRfaWQiOjUyNjgxMiwiYXBpX3VzZXJfaWQiOjcxNjI2MTk3LCJzZWNyZXQiOiJmZjBiZmJiM2UxYzc0MjY3YjIyZDIzOGYzMDBkNDhlYjhiNTnONPININONPN090MTg5Z**********************";
//    String secretKey = "eyJ2ZXJzaW9uIjoiUDJQIiwiZGF0YSI6eyJwYXlpbl9tZXJjaGFudF9zaXRlX3VpZCI6ImdlbTloeS0wMCIsInVzZXJfaWQiOiI3OTA0MzMwMjM4MiIsInNlY3JldCI6IjRiYTUyNTk1NTczNjAxNTY3MGVjOGVkNThiM2Q2MDhlZDMyZDIwMjE0ZDRhMzk0MTMzOGU1OTAwZTJhODQ5MDYifX0=";
    String secretKey = "eyJ2ZXJzaW9uIjoiUDJQIiwiZGF0YSI6eyJwYXlpbl9tZXJjaGFudF9zaXRlX3VpZCI6ImdpcnMwMi0wMCIsInVzZXJfaWQiOiI3OTA0Mzg4MDY2NiIsInNlY3JldCI6ImE0ZTQyOGE5NzUzYTY3YTM0MTBmNzFjNTVjNjk2ODJlNTFjZjUzOGY3MmYyMmE4MWY3NGMyMzdiZDQwYmU2NTAifX0=";
    private final BillPaymentClient client = BillPaymentClientFactory.createDefault(secretKey);

    public PaymentApi(OrderService orderService) {
        this.orderService = orderService;
    }
//    private final BillPaymentClient client = BillPaymentClientFactory.createDefault("payment.secretKey");

    public String payUrl(Order order) {
        CreateBillInfo billInfo = new CreateBillInfo(
                order.getId().toString(),
                new MoneyAmount(order.getTotal(),
                       Currency.getInstance("RUB")
                ),
                "oplata zakaza ", // + order.getDate(),
                ZonedDateTime.now().plusDays(3),
                new Customer(
                        order.getUser().getEmail(),
                        UUID.randomUUID().toString(),
                        order.getBuyerPhone()
                ),
                "http://localhost:8888");
        BillResponse billResponse = null;
        try {
            billResponse = client.createBill(billInfo);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        if (billResponse != null) {
            return billResponse.getPayUrl();
        }else {
            return null;
        }

    }

    @Async
    public void getStatus(String orderId) {
        String status = client.getBillInfo(orderId).getStatus().getValue().toString();
        // Когда будет готов OrderService здесь необходимо записать в статус "WAITING"(Ожидает оплаты)
        Order order = orderService.getByKey(Long.valueOf(orderId));
        //order.setPaymentStatus(PaymentStatus.WAITING);
       // orderService.update(order);

        while (!status.contains("PAID")) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            status = client.getBillInfo(orderId).getStatus().getValue().toString();
        }
        // А здесь записать в статус "PAID"(Оплачено)
//        Order order = orderService.getByKey(Long.valueOf(orderId));
        order.setPaymentStatus(PaymentStatus.PAID);
        orderService.update(order);
    }
}
