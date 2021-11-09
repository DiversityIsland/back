package com.amr.project.service.impl;

import com.amr.project.model.entity.*;
import com.amr.project.model.enums.Gender;
import com.amr.project.model.enums.PaymentStatus;
import com.amr.project.model.enums.Status;
import com.amr.project.service.abstracts.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.*;
import java.util.stream.IntStream;

@Service
@Transactional
public class TestDataEntityService {

    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CityService cityService;

    @Autowired
    private CountryService countryService;

    @Autowired
    private ItemService itemService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private ImageService imageService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private DiscountService discountService;

    @Autowired
    private OrderService orderService;


    public void createEntity() {
        createImageEntity();
        createCityEntity();
        createCountryEntity();
        createAddressEntity();
        createUserEntity();
        createCategoryEntity();
        createShopEntity();
        createItemEntity();
        createReviewEntity();
        createDiscountEntity();
        createOrderEntity();
    }

    @SneakyThrows
    private void createDiscountEntity() {
        Discount discount1 = Discount.builder()
                .fixedDiscount(1000)
                .percentage(0)
                .minOrder(5000)
                .shop(shopService.getByKey(1L))
                .user(userService.getByKey(2L))
                .build();
        discountService.persist(discount1);
        Discount discount2 = Discount.builder()
                .fixedDiscount(200)
                .percentage(0)
                .minOrder(2000)
                .shop(shopService.getByKey(2L))
                .user(userService.getByKey(2L))
                .build();
        discountService.persist(discount2);
        Discount discount3 = Discount.builder()
                .fixedDiscount(0)
                .percentage(5)
                .minOrder(3000)
                .shop(shopService.getByKey(3L))
                .user(userService.getByKey(2L))
                .build();
        discountService.persist(discount3);
        Discount discount4 = Discount.builder()
                .fixedDiscount(200)
                .percentage(0)
                .minOrder(1500)
                .shop(shopService.getByKey(2L))
                .user(userService.getByKey(2L))
                .build();
        discountService.persist(discount4);
        Discount discount5 = Discount.builder()
                .fixedDiscount(0)
                .percentage(15)
                .minOrder(5000)
                .shop(shopService.getByKey(4L))
                .user(userService.getByKey(2L))
                .build();
        discountService.persist(discount5);
        User user = userService.getByKey(2L);
        user.setDiscounts(new ArrayList<>(List.of(discount1, discount2, discount3, discount4, discount5)));
        userService.update(user);
    }

    @SneakyThrows
    private void createReviewEntity() {
        Review review1 = Review.builder()
                .text("Bad item!!!")
                .rating(1)
                .user(userService.getByKey(2L))
                .shop(shopService.getByKey(4L))
                .item(itemService.findItemById(12L))
                .isModerateAccept(false)
                .isModerated(false)
                .build();
        reviewService.persist(review1);
        Review review2 = Review.builder()
                .text("Good item!!!")
                .rating(5)
                .user(userService.getByKey(2L))
                .shop(shopService.getByKey(3L))
                .item(itemService.findItemById(12L))
                .isModerateAccept(false)
                .isModerated(false)
                .build();
        reviewService.persist(review2);
        User user = userService.getByKey(2L);
        user.setReviews(new ArrayList<>(List.of(review1, review2)));
        userService.update(user);
    }


    @SneakyThrows
    private void createImageEntity() {
        URL[] urls = new URL[26];
        //shops
        urls[0] = new URL("https://epicris.ru/wp-content/uploads/blackfriday/beru.jpg");
        urls[1] = new URL("https://epicris.ru/wp-content/uploads/blackfriday/lamoda.jpg");
        urls[2] = new URL("https://cdn.toy.ru/bitrix/templates/toy_2018/img/logo_forever.png");
        urls[3] = new URL("https://epicris.ru/wp-content/uploads/blackfriday/aliexpress.jpg");
        //items
        urls[4] = new URL("https://electrozon.ru/upload/resize/a2/a29cfb05d60445f0a31697029fe1be33_620x500.jpg");
        urls[5] = new URL("https://uk.louisvuitton.com/images/is/image/lv/1/PP_VP_L/louis-vuitton-transparent-3d-monogram-raincoat-ready-to-wear--FFCO16HSL900_PM2_Front%20view.jpg");
        urls[6] = new URL("https://img1.wbstatic.net/big/new/9760000/9765957-1.jpg");
        urls[7] = new URL("https://www.opticmall.ru/assets/images/products/3908/2132-622-17.jpg");
        urls[8] = new URL("https://pe-shop.ru/pictures/product/big/6045_big.jpg");
        urls[9] = new URL("https://cdn-images.farfetch-contents.com/13/29/99/20/13299920_14933337_480.jpg");
        urls[10] = new URL("https://cdn.rbt.ru/images/gen/item_image/image/8507/23/850668_r7634.jpg");
        urls[11] = new URL("https://www.ikea.com/ru/ru/images/products/friheten-ugl-div-krov-s-otd-d-hran-shiftebu-bezhevyy__0175606_pe328882_s5.jpg");
        urls[12] = new URL("https://www.ikea.com/ru/ru/images/products/vedbo-vedbu-stul-bereza-gunnared-klassicheskiy-seryy__0766041_pe753693_s5.jpg");
        urls[13] = new URL("https://www.ikea.com/ru/ru/images/products/klappa-myagkaya-igrushka-myach-raznocvetnyy__0873096_pe682670_s5.jpg?f=xxs");
        urls[14] = new URL("https://www.ikea.com/ru/ru/images/products/gulligast-gulligest-mobil__0923957_pe788456_s5.jpg");
        urls[15] = new URL("https://img.mvideo.ru/Pdb/20064335b.jpg");
        urls[16] = new URL("https://img.mvideo.ru/Pdb/20057032b1.jpg");
        urls[17] = new URL("https://static.onlinetrade.ru/img/items/s/blender_braun_mq_535_pogrugnoy_1.jpg");
        urls[18] = new URL("https://cdn1.ozone.ru/s3/multimedia-e/6004415198.jpg");
        urls[19] = new URL("https://static.onlinetrade.ru/img/items/m/samsung_galaxy_s21_ultra_5g_512gb_chernyy_fantom_1590946_5.jpg");
        urls[20] = new URL("https://drive-boom.ru/gallery/kia/photo/kia-ceed-2012-19008sm.jpeg");
        urls[21] = new URL("https://hundaj.ru/im/hendaj-solyaris-hetchbek/hendaj-solyaris-hetchbek-1.jpeg");
        urls[22] = new URL("https://static.onlinetrade.ru/img/items/b/botinki_timberland_tbla2eduw_zhenskie_tsvet_korichnevyy_razmer_6_1527950_2.jpg");
        urls[23] = new URL("https://i.pinimg.com/originals/a8/f8/53/a8f853e94e56ed8da0e7b766a2202408.jpg");
        urls[24] = new URL("https://news.store.rambler.ru/img/c3b030d73abf4a2716af03afa01794bd");
        urls[25] = new URL("https://catherineasquithgallery.com/uploads/posts/2021-03/1614596205_33-p-muzhchina-kartinka-na-belom-fone-36.jpg");

        for (URL url:
             urls) {
            Image image = Image.builder()
                    .url(url.toString())
                    .picture(downloadImage(url))
                    .isMain(true)
                    .build();
            imageService.persist(image);
        }

    }

    public static byte[] downloadImage(URL url) {
        byte[] bytes = null;
        try {
            BufferedImage originalImage= ImageIO.read(url);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ImageIO.write(originalImage, "jpg", baos);
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    private void createCityEntity() {
        Set<String> cities = new HashSet<>();
        cities.add("Moscow");
        cities.add("Rostov");
        cities.add("Tver");
        cities.add("Minsk");
        cities.add("Gomel");
        cities.add("Brest");
        cities.add("Kiev");
        cities.add("Zaporozhye");
        cities.add("Odessa");
        cities.add("London");
        cities.add("Liverpool");
        cities.add("Birmingham");
        cities.add("HongKong");

        for (String c:
             cities) {
            City city = City.builder()
                    .name(c)
                    .build();
            cityService.persist(city);
        }
    }

    private void createCountryEntity() {
        Set<City> cities = new HashSet<>();
        cities.add(cityService.getByName("Moscow"));
        cities.add(cityService.getByName("Rostov"));
        cities.add(cityService.getByName("Tver"));

        Country country = Country.builder()
                .name("Russia")
                .cities(cities)
                .build();
        countryService.persist(country);

        cities.clear();
        cities.add(cityService.getByName("Minsk"));
        cities.add(cityService.getByName("Gomel"));
        cities.add(cityService.getByName("Brest"));

        Country country1 = Country.builder()
                .name("Belarus")
                .cities(cities)
                .build();
        countryService.persist(country1);

        cities.clear();
        cities.add(cityService.getByName("Kiev"));
        cities.add(cityService.getByName("Zaporozhye"));
        cities.add(cityService.getByName("Odessa"));

        Country country2 = Country.builder()
                .name("Ukraine")
                .cities(cities)
                .build();
        countryService.persist(country2);

        cities.clear();
        cities.add(cityService.getByName("London"));
        cities.add(cityService.getByName("Liverpool"));
        cities.add(cityService.getByName("Birmingham"));

        Country country3 = Country.builder()
                .name("England")
                .cities(cities)
                .build();
        countryService.persist(country3);

        cities.clear();
        cities.add(cityService.getByName("HongKong"));

        Country country4 = Country.builder()
                .name("China")
                .cities(cities)
                .build();
        countryService.persist(country4);

        Country country5 = Country.builder()
                .name("Angola")
                .cities(null)
                .build();
        countryService.persist(country5);
    }

    private void createAddressEntity() {
        Address address = Address.builder()
                .cityIndex("159726")
                .country(countryService.getByName("Russia"))
                .city(cityService.getByName("Moscow"))
                .street("Arbat")
                .house("12")
                .build();
        addressService.persist(address);

        Address address1 = Address.builder()
                .cityIndex("2225588")
                .country(countryService.getByName("Russia"))
                .city(cityService.getByName("Rostov"))
                .street("Lenina")
                .house("5")
                .build();
        addressService.persist(address1);

        Address address2 = Address.builder()
                .cityIndex("7755882")
                .country(countryService.getByName("England"))
                .city(cityService.getByName("London"))
                .street("Baker")
                .house("221B")
                .build();
        addressService.persist(address2);

        Address address3 = Address.builder()
                .cityIndex("568679")
                .country(countryService.getByName("China"))
                .city(cityService.getByName("HongKong"))
                .street("Stroiteley")
                .house("19")
                .build();
        addressService.persist(address3);
    }


    private void createUserEntity() {
        Role adminRole = Role.builder()
                .name("ADMIN")
                .build();

        Role userRole = Role.builder()
                .name("USER")
                .build();

        Role unregisteredRole = Role.builder()
                .name("UNREGISTERED")
                .build();

        Role moderatorRole = Role.builder()
                .name("MODERATOR")
                .build();

        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        roles.add(userRole);
        roles.add(unregisteredRole);
        roles.add(moderatorRole);


        User admin = User.builder()
                .email("admin@admin.ru")
                .username("admin")
                .password("admin1")
                .activate(true)
                .activationCode("admin")
                .phone("111-11-11-11")
                .firstName("Admin")
                .lastName("Admin")
                .age(50)
                .address(addressService.getByKey(1L))
                .roles(roles)
                .gender(Gender.MALE)
                .birthday(new GregorianCalendar(1971, 1, 1))
                .build();
        userService.persist(admin);

        roles.clear();
        roles.add(userRole);

        User user = User.builder()
                .email("user@user.ru")
                .password("user00")
                .username("user")
                .activate(true)
                .activationCode("user")
                .phone("222-22-22-22")
                .firstName("user")
                .lastName("user")
                .age(20)
                .address(addressService.getByKey(2L))
                .roles(roles)
                .gender(Gender.MALE)
                .birthday(new GregorianCalendar(2001, 3, 12))
                .build();
        userService.persist(user);

        User user1 = User.builder()
                .email("user1@user.ru")
                .password("user11")
                .username("user1")
                .activate(true)
                .activationCode("user1")
                .phone("333-33-33-33")
                .firstName("user1")
                .lastName("user1")
                .age(35)
                .address(addressService.getByKey(3L))
                .roles(roles)
                .gender(Gender.UNKNOWN)
                .birthday(new GregorianCalendar(2001, 12, 4))
                .build();
        userService.persist(user1);

        User user2 = User.builder()
                .email("user2@user.ru")
                .password("user22")
                .username("user2")
                .activate(true)
                .activationCode("user2")
                .phone("444-44-44-44")
                .firstName("user2")
                .lastName("user2")
                .age(90)
                .address(addressService.getByKey(4L))
                .roles(roles)
                .gender(Gender.FEMALE)
                .birthday(new GregorianCalendar(1955, 5, 7))
                .build();
        userService.persist(user2);
    }

    private void createCategoryEntity() {
        List<String> categories = new ArrayList<>();
        categories.add("Одежда");
        categories.add("Обувь");
        categories.add("Аксессуары");
        categories.add("Мебель");
        categories.add("Бытовая техника");
        categories.add("Детские товары");
        categories.add("Компьютеры");
        categories.add("Мобильные телефоны");
        categories.add("Автомобили");

        for (String s:
             categories) {
            Category category = Category.builder()
                    .name(s)
                    .build();
            categoryService.persist(category);
        }
    }

    private void createShopEntity() {

        Shop shop = Shop.builder()
                .name("Яндекс Маркет")
                .email("info@yandex.ru")
                .phone("789-464-55-55")
                .description("Одежда, обувь, бытовая техника")
                .location(countryService.getByName("Russia"))
                .logo(imageService.getByKey(1L))
                .count(1)
                .rating(1)
                .user(userService.getByKey(2L))
                .isModerated(true)
                .isModerateAccept(true)
                .activity(1)
                .build();
        shopService.persist(shop);

        Shop shop1 = Shop.builder()
                .name("Lamoda")
                .email("info@lamoda.ru")
                .phone("1535-156-4454")
                .description("Одежда, обувь, аксессуары")
                .location(countryService.getByName("Russia"))
                .logo(imageService.getByKey(2L))
                .count(1)
                .rating(2)
                .user(userService.getByKey(2L))
                .isModerated(true)
                .isModerateAccept(true)
                .activity(1)
                .build();
        shopService.persist(shop1);

        Shop shop2 = Shop.builder()
                .name("Toy.ru")
                .email("info@toy.ru")
                .phone("1564-561-44")
                .description("Игрушки")
                .location(countryService.getByName("Belarus"))
                .logo(imageService.getByKey(3L))
                .count(1)
                .rating(3)
                .user(userService.getByKey(3L))
                .isModerated(true)
                .isModerateAccept(false)
                .activity(1)
                .build();
        shopService.persist(shop2);

        Shop shop3 = Shop.builder()
                .name("AliExpress")
                .email("info@aliexpress.ru")
                .phone("54-454654-45645")
                .description("Любые товары")
                .location(countryService.getByName("China"))
                .logo(imageService.getByKey(4L))
                .count(1)
                .rating(4)
                .user(userService.getByKey(4L))
                .isModerated(true)
                .isModerateAccept(true)
                .activity(1)
                .build();
        shopService.persist(shop3);
        Shop shop4 = Shop.builder()
                .name("Some bad guys shop")
                .email("worstshopever@mail.ru")
                .phone("88005553535")
                .description("Воруем деньги!")
                .location(countryService.getByName("China"))
                .logo(imageService.getByKey(25L))
                .user(userService.getByKey(1L))
                .isModerated(false)
                .isModerateAccept(false)
                .build();
        shopService.persist(shop4);
        Shop shop5 = Shop.builder()
                .name("Some good guys shop")
                .email("wonderfulshop@gmail.com")
                .phone("7-903-271-4421")
                .description("Отличные товары по отличным ценам!")
                .location(countryService.getByName("Russia"))
                .logo(imageService.getByKey(26L))
                .user(userService.getByKey(1L))
                .isModerated(false)
                .isModerateAccept(false)
                .build();
        shopService.persist(shop5);
    }

    private void createItemEntity() {
        Set<Category> categories = new HashSet<>();
        categories.add(categoryService.getByKey(1L));

        Set<Image> images = new HashSet<>();
        images.add(imageService.getByKey(5L));

        Shop[] shops = new Shop[4];
        for (int i = 0; i < 4; i++) {
            shops[i] = shopService.getByKey(Long.valueOf(i+1));
        }

        Item item = Item.builder()
                .name("Маска медицинская")
                .basePrice(BigDecimal.valueOf(10))
                .price(BigDecimal.valueOf(30))
                .categories(categories)
                .images(images)
                .count(100)
                .rating(1)
                .description("Маска медицинская")
                .shop(shops[0])
                .isModerated(false)
                .isModerateAccept(false)
                .build();
        itemService.persist(item);

        images.clear();
        images.add(imageService.getByKey(6L));
        Item item3 = Item.builder()
                .name("Плащ Louis Vuitton")
                .basePrice(BigDecimal.valueOf(15000))
                .price(BigDecimal.valueOf(150000))
                .categories(categories)
                .images(images)
                .count(10)
                .rating(4)
                .description("Плащ Louis Vuitton")
                .shop(shops[1])
                .isModerated(false)
                .isModerateAccept(false)
                .build();
        itemService.persist(item3);

        categories.clear();
        categories.add(categoryService.getByKey(3L));

        images.clear();
        images.add(imageService.getByKey(7L));

        Item item18 = Item.builder()
                .name("Зонт 3 слона")
                .basePrice(BigDecimal.valueOf(400))
                .price(BigDecimal.valueOf(700))
                .categories(categories)
                .images(images)
                .count(19)
                .rating(19)
                .description("Зонт 3 слона")
                .shop(shops[3])
                .isModerated(false)
                .isModerateAccept(false)
                .build();
        itemService.persist(item18);

        images.clear();
        images.add(imageService.getByKey(8L));
        Item item19 = Item.builder()
                .name("Очки RAY BAN")
                .basePrice(BigDecimal.valueOf(8000))
                .price(BigDecimal.valueOf(15000))
                .categories(categories)
                .images(images)
                .count(30)
                .rating(20)
                .description("Очки RAY BAN")
                .shop(shops[1])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item19);

        images.clear();
        images.add(imageService.getByKey(9L));
        Item item1 = Item.builder()
                .name("Антисептик для рук")
                .basePrice(BigDecimal.valueOf(45))
                .price(BigDecimal.valueOf(150))
                .categories(categories)
                .images(images)
                .count(100)
                .rating(2)
                .description("Антисептик для рук")
                .shop(shops[0])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item1);

        images.clear();
        images.add(imageService.getByKey(10L));
        Item item4 = Item.builder()
                .name("Сумка Birkin")
                .basePrice(BigDecimal.valueOf(10000))
                .price(BigDecimal.valueOf(1250000))
                .categories(categories)
                .images(images)
                .count(2)
                .rating(5)
                .description("Сумка Birkin")
                .shop(shops[1])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item4);

        categories.clear();
        categories.add(categoryService.getByKey(7L));

        images.clear();
        images.add(imageService.getByKey(11L));

        Item item2 = Item.builder()
                .name("Ноутбук Lenovo")
                .basePrice(BigDecimal.valueOf(30000))
                .price(BigDecimal.valueOf(70000))
                .categories(categories)
                .images(images)
                .count(27)
                .rating(3)
                .description("Ноутбук Lenovo")
                .shop(shops[3])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item2);


        categories.clear();
        categories.add(categoryService.getByKey(4L));

        images.clear();
        images.add(imageService.getByKey(12L));
        Item item5 = Item.builder()
                .name("Диван ФРИХЕТЭН")
                .basePrice(BigDecimal.valueOf(16000))
                .price(BigDecimal.valueOf(24999))
                .categories(categories)
                .images(images)
                .count(8)
                .rating(6)
                .description("Диван ФРИХЕТЭН")
                .shop(shops[1])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item5);

        images.clear();
        images.add(imageService.getByKey(13L));
        Item item6 = Item.builder()
                .name("Кресло Ведбу")
                .basePrice(BigDecimal.valueOf(10000))
                .price(BigDecimal.valueOf(16999))
                .categories(categories)
                .images(images)
                .count(4)
                .rating(7)
                .description("Кресло Ведбу")
                .shop(shops[2])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item6);

        categories.clear();
        categories.add(categoryService.getByKey(6L));

        images.clear();
        images.add(imageService.getByKey(14L));

        Item item7 = Item.builder()
                .name("Мягкая игрушка")
                .basePrice(BigDecimal.valueOf(700))
                .price(BigDecimal.valueOf(1000))
                .categories(categories)
                .images(images)
                .count(99)
                .rating(8)
                .description("Мягкая игрушка")
                .shop(shops[1])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item7);


        images.clear();
        images.add(imageService.getByKey(15L));
        Item item8 = Item.builder()
                .name("Мобиль")
                .basePrice(BigDecimal.valueOf(400))
                .price(BigDecimal.valueOf(649))
                .categories(categories)
                .images(images)
                .count(40)
                .rating(9)
                .description("Мобиль")
                .shop(shops[2])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item8);

        categories.clear();
        categories.add(categoryService.getByKey(5L));

        images.clear();
        images.add(imageService.getByKey(16L));

        Item item9 = Item.builder()
                .name("Стиральная машина Bosch")
                .basePrice(BigDecimal.valueOf(80000))
                .price(BigDecimal.valueOf(200000))
                .categories(categories)
                .images(images)
                .count(9)
                .rating(10)
                .description("Стиральная машина Bosch")
                .shop(shops[2])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item9);

        images.clear();
        images.add(imageService.getByKey(17L));
        Item item10 = Item.builder()
                .name("Кофемашина Philips")
                .basePrice(BigDecimal.valueOf(40000))
                .price(BigDecimal.valueOf(84999))
                .categories(categories)
                .images(images)
                .count(3)
                .rating(11)
                .description("Кофемашина Philips")
                .shop(shops[2])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item10);

        images.clear();
        images.add(imageService.getByKey(18L));
        Item item11 = Item.builder()
                .name("Блендер Braun")
                .basePrice(BigDecimal.valueOf(1000))
                .price(BigDecimal.valueOf(3700))
                .categories(categories)
                .images(images)
                .count(15)
                .rating(12)
                .description("Блендер Braun")
                .shop(shops[0])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item11);

        categories.clear();
        categories.add(categoryService.getByKey(8L));

        images.clear();
        images.add(imageService.getByKey(19L));

        Item item12 = Item.builder()
                .name("Телефон Nokia")
                .basePrice(BigDecimal.valueOf(5000))
                .price(BigDecimal.valueOf(10999))
                .categories(categories)
                .images(images)
                .count(25)
                .rating(13)
                .description("Телефон Nokia")
                .shop(shops[3])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item12);

        images.clear();
        images.add(imageService.getByKey(20L));

        Item item13 = Item.builder()
                .name("Телефон Samsung")
                .basePrice(BigDecimal.valueOf(70000))
                .price(BigDecimal.valueOf(118990))
                .categories(categories)
                .images(images)
                .count(10)
                .rating(14)
                .description("Телефон Samsung")
                .shop(shops[3])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item13);

        categories.clear();
        categories.add(categoryService.getByKey(9L));

        images.clear();
        images.add(imageService.getByKey(21L));
        Item item14 = Item.builder()
                .name("Автомобиль Kia Ceed")
                .basePrice(BigDecimal.valueOf(600000))
                .price(BigDecimal.valueOf(1599999))
                .categories(categories)
                .images(images)
                .count(1)
                .rating(15)
                .description("Автомобиль Kia Ceed")
                .shop(shops[0])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item14);

        images.clear();
        images.add(imageService.getByKey(22L));

        Item item15 = Item.builder()
                .name("Автомобиль Hundai Solaris")
                .basePrice(BigDecimal.valueOf(800000))
                .price(BigDecimal.valueOf(1250000))
                .categories(categories)
                .images(images)
                .count(1)
                .rating(16)
                .description("Автомобиль Hundai Solaris")
                .shop(shops[0])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item15);

        categories.clear();
        categories.add(categoryService.getByKey(2L));

        images.clear();
        images.add(imageService.getByKey(23L));

        Item item16 = Item.builder()
                .name("Ботинки Timberland")
                .basePrice(BigDecimal.valueOf(4000))
                .price(BigDecimal.valueOf(10000))
                .categories(categories)
                .images(images)
                .count(4)
                .rating(17)
                .description("Ботинки Timberland")
                .shop(shops[0])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item16);

        images.clear();
        images.add(imageService.getByKey(24L));
        Item item17 = Item.builder()
                .name("Ботинки Dr.Martens")
                .basePrice(BigDecimal.valueOf(8000))
                .price(BigDecimal.valueOf(15999))
                .categories(categories)
                .images(images)
                .count(7)
                .rating(18)
                .description("Ботинки Dr.Martens")
                .shop(shops[3])
                .isModerated(true)
                .isModerateAccept(true)
                .build();
        itemService.persist(item17);
    }

    private void createOrderEntity() {
        User user1 = userService.getByKey(2L);
        User user2 = userService.getByKey(3L);
        User user3 = userService.getByKey(4L);

        List<Item> allItems = new ArrayList<>(itemService.getAll());
        allItems.removeAll(itemService.getUnmoderatedItems());

        Set<Item> itemSet1 = new HashSet<>(allItems.subList(0, 3));
        Set<Item> itemSet2 = new HashSet<>(allItems.subList(2, 5));
        Set<Item> itemSet3 = new HashSet<>(allItems.subList(1, 8));
        Set<Item> itemSet4 = new HashSet<>(allItems.subList(3, 12));
        Set<Item> itemSet5 = new HashSet<>(allItems.subList(0, 11));
        Set<Item> itemSet6 = new HashSet<>(allItems.subList(7, 10));

        Order order1 = Order.builder()
                .items(itemSet1)
                .date(Calendar.getInstance())
                .status(Status.COMPLETE)
                .address(addressService.getByKey(1L))
                .total(BigDecimal.valueOf(itemSet1.stream()
                        .mapToInt(item -> item.getPrice().intValue()).sum()))
                .user(user1)
                .buyerName(user1.getFirstName())
                .buyerPhone(user1.getPhone())
                .paymentStatus(PaymentStatus.PAYED)
                .build();
        orderService.persist(order1);

        Order order2 = Order.builder()
                .items(itemSet2)
                .date(Calendar.getInstance())
                .status(Status.COMPLETE)
                .address(addressService.getByKey(1L))
                .total(BigDecimal.valueOf(itemSet2.stream()
                        .mapToInt(item -> item.getPrice().intValue()).sum()))
                .user(user2)
                .buyerName(user2.getFirstName())
                .buyerPhone(user2.getPhone())
                .paymentStatus(PaymentStatus.WAITING)
                .build();
        orderService.persist(order2);

        Order order3 = Order.builder()
                .items(itemSet3)
                .date(Calendar.getInstance())
                .status(Status.COMPLETE)
                .address(addressService.getByKey(1L))
                .total(BigDecimal.valueOf(itemSet3.stream()
                        .mapToInt(item -> item.getPrice().intValue()).sum()))
                .user(user3)
                .buyerName(user3.getFirstName())
                .buyerPhone(user3.getPhone())
                .paymentStatus(PaymentStatus.PAYED)
                .build();
        orderService.persist(order3);

        Order order4 = Order.builder()
                .items(itemSet4)
                .date(Calendar.getInstance())
                .status(Status.START)
                .address(addressService.getByKey(1L))
                .total(BigDecimal.valueOf(itemSet4.stream()
                        .mapToInt(item -> item.getPrice().intValue()).sum()))
                .user(user1)
                .buyerName(user1.getFirstName())
                .buyerPhone(user1.getPhone())
                .paymentStatus(PaymentStatus.PAYED)
                .build();
        orderService.persist(order4);

        Order order5 = Order.builder()
                .items(itemSet5)
                .date(Calendar.getInstance())
                .status(Status.COMPLETE)
                .address(addressService.getByKey(1L))
                .total(BigDecimal.valueOf(itemSet5.stream()
                        .mapToInt(item -> item.getPrice().intValue()).sum()))
                .user(user2)
                .buyerName(user2.getFirstName())
                .buyerPhone(user2.getPhone())
                .paymentStatus(PaymentStatus.PAYED)
                .build();
        orderService.persist(order5);

        Order order6 = Order.builder()
                .items(itemSet6)
                .date(Calendar.getInstance())
                .status(Status.COMPLETE)
                .address(addressService.getByKey(1L))
                .total(BigDecimal.valueOf(itemSet6.stream()
                        .mapToInt(item -> item.getPrice().intValue()).sum()))
                .user(user3)
                .buyerName(user3.getFirstName())
                .buyerPhone(user3.getPhone())
                .paymentStatus(PaymentStatus.WAITING)
                .build();
        orderService.persist(order6);

    }

}
