package com.amr.project.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;

@Entity
@Table(name = "item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiIgnore
@Builder
@ToString(of = {"id", "name", "price"})
public class Item {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private BigDecimal basePrice;

    @Column
    private BigDecimal price;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinTable(name = "item_category",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "category_id")})
    private Collection<Category> categories;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "item_image",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "image_id")})
    private Collection<Image> images;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "item_review",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "review_id")})
    private Collection<Review> reviews;

    @Column
    private int count;

    @Column
    private double rating;

    @Column
    private String description;

    @Column
    private int discount;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "shop_item",
            joinColumns = {@JoinColumn(name = "item_id")},
            inverseJoinColumns = {@JoinColumn(name = "shop_id")})
    private Shop shop;

    @Column(name = "is_moderated")
    private boolean isModerated = false;

    @Column(name = "is_moderate_accept")
    private boolean isModerateAccept = false;

    @Column(name = "moderated_reject_reason")
    private String moderatedRejectReason;

    @Column(name = "pretendent_to_be_deleted")
    private boolean isPretendentToBeDeleted;

}
