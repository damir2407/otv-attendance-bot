package org.example.otvattendancebotnode.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "id")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = false)
    private String lastName;

    @Column(length = 64)
    private String telegramName;

    @Column(nullable = false)
    private Long telegramChatId;

    @Column(nullable = false)
    private Long telegramUserId;

    @Column(nullable = false)
    private boolean isNotificationEnabled;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    public Student(String firstName, String lastName, String telegramName, Long telegramChatId,
        Long telegramUserId,
        boolean isNotificationEnabled, Group group) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telegramName = telegramName;
        this.telegramChatId = telegramChatId;
        this.telegramUserId = telegramUserId;
        this.isNotificationEnabled = isNotificationEnabled;
        this.group = group;
    }
}
