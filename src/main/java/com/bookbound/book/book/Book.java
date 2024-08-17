package com.bookbound.book.book;

import com.bookbound.book.common.BaseEntity;
import com.bookbound.book.feedback.Feedback;
import com.bookbound.book.history.BookTransactionHistory;
import com.bookbound.book.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Book extends BaseEntity {

    @Column(name= "TITLE")
    private String title;
    @Column(name= "AUTHORNAME")
    private String authorName;
    @Column(name= "ISBN")
    private String isbn;
    @Column(name= "SYNOPSIS", length=2000)
    private String synopsis;
    @Column(name= "BOOKCOVER")
    private String bookCover;
    @Column(name= "ARCHIVED")
    private boolean archived;
    @Column(name= "SHAREABLE")
    private boolean shareable;
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToMany(mappedBy = "book")
    private List<Feedback> feedbacks;
    @OneToMany(mappedBy = "book")
    private List<BookTransactionHistory> histories;

    @Transient
    public double getRate() {
        if (feedbacks == null || feedbacks.isEmpty()) {
            return 0.0;
        }
        var rate = this.feedbacks.stream()
                .mapToDouble(Feedback::getNote)
                .average()
                .orElse(0.0);
        double roundedRate = Math.round(rate * 10.0) / 10.0;

        // Return 4.0 if roundedRate is less than 4.5, otherwise return 4.5
        return roundedRate;
    }
}
