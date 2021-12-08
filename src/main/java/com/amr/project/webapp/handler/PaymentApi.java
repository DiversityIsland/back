package com.amr.project.webapp.handler;

import com.amr.project.model.entity.Order;
import com.qiwi.billpayments.sdk.client.BillPaymentClient;
import com.qiwi.billpayments.sdk.client.BillPaymentClientFactory;
import com.qiwi.billpayments.sdk.model.MoneyAmount;
import com.qiwi.billpayments.sdk.model.in.CreateBillInfo;
import com.qiwi.billpayments.sdk.model.in.Customer;
import com.qiwi.billpayments.sdk.model.out.BillResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.Currency;
import java.util.UUID;

@Component
public class PaymentApi {
    private final BillPaymentClient client = BillPaymentClientFactory.createDefault("payment.secretKey");

    public String payUrl(Order order) {
        CreateBillInfo billInfo = new CreateBillInfo(
                order.getId().toString(),
                new MoneyAmount(order.getTotal(),
                        Currency.getInstance("currency")
                ),
                "oplata zakaza " + order.getDate(),
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
        // Когда будет готов OrderService здесь необходимо записать встатус "WAITING"(Ожидает оплаты)
        while (!status.contains("COMPLETE")) {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            status = client.getBillInfo(orderId).getStatus().getValue().toString();
        }
        // А здесь записать в статус "COMPLETE"(Оплачено)
    }
}
