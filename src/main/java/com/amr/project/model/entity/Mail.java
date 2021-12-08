package com.amr.project.model.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.UUID;

@NoArgsConstructor
@Data
public class Mail {

    private String to;
    private String from;
    private String subject;
    private String message;

    public Mail(String to, String subject, String message) {
        this.to = to;
        this.subject = subject;
        this.message = message;
    }

    public static String generateActivationCode() {
        return UUID.randomUUID().toString();
    }

    public static Mail activationMail(User user) {
        Mail mail = new Mail();
        String message = String.format(
                "Здравствуйте, %s! \n" +
                        "Добро пожаловать на наш сайт по продаже товаров! \n"
                        + "Просим пройти активацию по ссылке \n" +
                        "http://localhost:8888/activate/%s", user.getFirstName(), user.getActivationCode()
        );
        mail.setTo(user.getEmail());
        mail.setSubject("Activation Email");
        mail.setMessage(message);
        return mail;
    }

    public static Mail changePassword(String email, String name) {
        Mail mail = new Mail();
        mail.setTo(email);
        mail.setSubject("Изменение пароля");
        mail.setMessage(String.format("Уважаемый пользователь %s!\n" +
                "Ваш пароль был изменен!", name));
        return mail;
    }

    public static Mail changeUser(User originalUser, User changeUser) {
        Mail mail = new Mail();
        String message = mail.changeMainFiledUserMessage(originalUser, changeUser);
        if (message == "") return null;
        if (originalUser.getEmail().equals(changeUser.getEmail()))
            mail.setTo(originalUser.getEmail());
        else
            mail.setTo(changeUser.getEmail());
        mail.setSubject("Изменение данных");
        mail.setMessage(message);
        return mail;
    }

    public static Mail changeUser(User originalUser, User changeUser, Address address) {
        Mail mail = new Mail();
        String message = mail.changeMainFiledUserMessage(originalUser, changeUser, address);
        if (message == "") return null;
        if (originalUser.getEmail().equals(changeUser.getEmail()))
            mail.setTo(originalUser.getEmail());
        else
            mail.setTo(changeUser.getEmail());
        mail.setSubject("Изменение данных");
        mail.setMessage(message);
        return mail;
    }

    public static Mail deleteUser(String email, String name) {
        Mail mail = new Mail();
        String message = String.format("Уважаемый, %s! \n" +
                "Ваш аккаунт удален!", name
        );
        mail.setTo(email);
        mail.setSubject("Удаление аккаунта");
        mail.setMessage(message);
        return mail;
    }

    public static Mail isModerateShop(Shop shop) {
        Mail mail = new Mail();
        if (Objects.nonNull(shop.getEmail())) {
            mail.setTo(shop.getEmail());
        } else {
            mail.setTo(shop.getUser().getEmail());
        }
        mail.setSubject(String.format("Модерация магазина \"%s\"", shop.getName()));
        mail.setMessage(String.format("Ваш магазин \"%s!\" \n" +
                "Прошел проверку модератором!", shop.getName()));
        return mail;
    }

    public static Mail createShop(Shop shop) {
        Mail mail = new Mail();
        if (Objects.nonNull(shop.getEmail())) {
            mail.setTo(shop.getEmail());
        } else {
            mail.setTo(shop.getUser().getEmail());
        }
        mail.setSubject(String.format("Создание магазина \"%s\"", shop.getName()));
        mail.setMessage(String.format("Ваш магазин \"%s!\" \n" +
                "Был создан и отправлен на проверку модератору", shop.getName()));
        return mail;
    }

    public static Mail statusOrder(Order order) {
        Mail mail = new Mail();
        String status = "";
        mail.setTo(order.getUser().getEmail());
        if (order.getStatus() == null) {
            return null;
        }
        switch (order.getStatus()) {
            case START:
                status = "Оформление";
                break;
            case COMPLETE:
                status = "Укомплектован";
                break;
            case SENT:
                status = "Отправлен";
                break;
            case DELIVERED:
                status = "Доставлен";
                break;
        }
        mail.setSubject(String.format("Ваш заказ №%s изменил статус заказа", order.getId()));
        mail.setMessage(String.format("Уважаемый пользователь %s! \n" +
                "Ваш заказ №%s перешел в статус %s", order.getUser().getFirstName(), order.getId(), status));
        return mail;
    }

    private String changeMainFiledUserMessage(User originalUser, User changeUser) {
        StringBuilder message = new StringBuilder(String.format(
                "Пользователь %s! \n" + "Ваши следующие данные были изменены: \n",
                originalUser.getUsername()));
        boolean wereChanges = false;
        if (!Objects.equals(originalUser.getUsername(), changeUser.getUsername()) && !changeUser.getUsername().equals("")) {
            message.append(String.format("Username с %s на %s \n", originalUser.getUsername(), changeUser.getUsername()));
            wereChanges = true;
        }
        if (!Objects.equals(originalUser.getFirstName(), changeUser.getFirstName()) && !changeUser.getFirstName().equals("")) {
            message.append(String.format("Имя с %s на %s \n", originalUser.getFirstName(), changeUser.getFirstName()));
            wereChanges = true;
        }
        if (!Objects.equals(originalUser.getLastName(), changeUser.getLastName()) && !changeUser.getLastName().equals("")) {
            message.append(String.format("Фамилия с %s на %s \n", originalUser.getLastName(), changeUser.getLastName()));
            wereChanges = true;
        }
        if (!Objects.equals(originalUser.getEmail(), changeUser.getEmail()) && !changeUser.getEmail().equals("")) {
            message.append(String.format("Почтовый адрес с %s на %s \n", originalUser.getEmail(), changeUser.getEmail()));
            wereChanges = true;
        }
        if (!Objects.equals(originalUser.getPhone(), changeUser.getPhone()) && !changeUser.getPhone().equals("")) {
            message.append(String.format("Телефон с %s на %s \n", originalUser.getPhone(), changeUser.getPhone()));
            wereChanges = true;
        }
        if (!originalUser.getBirthday().equals(changeUser.getBirthday()) && Objects.nonNull(changeUser.getBirthday())) {
            message.append(String.format("Дата рождения с %s на %s \n", originalUser.getBirthday(), changeUser.getBirthday()));
            wereChanges = true;
        }
        return !wereChanges ? "" : message.toString();
    }

    private String changeMainFiledUserMessage(User originalUser, User changeUser, Address address) {
        StringBuilder message = new StringBuilder();
        boolean wereChanges = false;
        message.append(this.changeMainFiledUserMessage(originalUser, changeUser));
        if (message.toString() != "") {
            wereChanges = true;
        }
        if (!originalUser.getAddress().equals(address) && Objects.nonNull(address)) {
            message.append((String.format("Ваш адрес был изменен на: \n" +
                    "%s", "Индекс: " + address.getCityIndex() + " Страна " +
                    address.getCountry().getName() +
                    " Город: " + address.getCity().getName() +
                    " Улица: " + address.getStreet() +
                    " Дом: " + address.getHouse())));
            wereChanges = true;
        }
        return !wereChanges ? "" : message.toString();
    }


}
