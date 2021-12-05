package com.amr.project.model.entity;

import com.amr.project.model.enums.Gender;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.jboss.aerogear.security.otp.api.Base32;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Collection;

@Entity
@Table(name = "user")
@AllArgsConstructor
@ApiIgnore
@Getter
@Setter
@Builder
@ToString(of = {"id", "email", "username", "password", "phone", "firstName", "lastName", "age", "gender"})
@EqualsAndHashCode(of = {"id", "email", "username"})
public class User implements UserDetails {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @org.hibernate.validator.constraints.NotBlank(message = "Введите ваш email")
    @Email(message = "Введите корректный email")
    private String email;


    @Column(unique = true)
    @NotBlank(message = "Введите имя пользователя")
    @Length(min = 4, message = "Имя пользователя должно быть не менее 4 символов")
    private String username;

    @Column
    @NotBlank(message = "Введите пароль")
    @Length(min = 4, message = "пароль должен быть не менее 6 символов")
    private String password;

    private boolean activate;

    @Column(name = "activation_code")
    private String activationCode;

    @Column(unique = true)
    @Length(min = 11, message = "phone number must be at least 11 characters")
    private String phone;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column
    private int age;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id",referencedColumnName = "id")
    private Address address;

    @ManyToMany(fetch = FetchType.LAZY, targetEntity = Role.class, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
            )
    private Collection<Role> roles;

    @Column
    private Gender gender;

    @Column
    private Calendar birthday;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id",referencedColumnName = "id")
    private Image images;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_coupon",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "coupon_id")})
    private Collection<Coupon> coupons;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_cart",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "cart_id")})
    private Collection<CartItem> cart;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_orders")
    private Collection<Order> orders;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "user_review",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "review_id")})
    private Collection<Review> reviews;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "user_shop",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "shop_id")})
    private Collection<Shop> shops;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Favorite favorite;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JoinTable(name = "user_discount",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "discount_id")})
    private Collection<Discount> discounts;

    private boolean isUsingTwoFactorAuth;

    private String secret;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User() {
        secret = Base32.random();
    }

    public User(Long id, String email, String username) {
        this.id = id;
        this.email = email;
        this.username = username;
    }

    public User(Long id, String username, Collection<Role> roles){
        this.id = id;
        this.username = username;
        this.roles = roles;
    }

    public int calculateAge() {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = this.getBirthday();

        int age = 0;

        if (birthDate.after(today)) {
            throw new IllegalArgumentException("Can't be born in the future");
        }

        age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR);

        if ( (birthDate.get(Calendar.DAY_OF_YEAR) - today.get(Calendar.DAY_OF_YEAR) > 3) ||
                (birthDate.get(Calendar.MONTH) > today.get(Calendar.MONTH ))){
            age--;

        }else if ((birthDate.get(Calendar.MONTH) == today.get(Calendar.MONTH )) &&
                (birthDate.get(Calendar.DAY_OF_MONTH) > today.get(Calendar.DAY_OF_MONTH ))){
            age--;
        }

        return age;
    }
}
