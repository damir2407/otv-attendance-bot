package org.example.otvattendancebotnode.entity;

import java.util.Objects;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64, nullable = false)
    private String firstName;

    @Column(length = 64, nullable = false)
    private String lastName;

    @Column(length = 64, unique = true)
    private String telegramName;

    @Column(nullable = false, unique = true)
    private Long telegramChatId;

    @Column(nullable = false, unique = true)
    private Long telegramUserId;

    @Column(nullable = false, unique = true)
    private Long personnelNumber;

    @Column(nullable = false)
    private boolean isNotificationEnabled;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @OneToMany(mappedBy = "student", fetch = FetchType.EAGER)
    private Set<Attendance> attendances;

    @Override
    public int hashCode() {
        return Objects.hash(personnelNumber);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Student that = (Student) o;
        return Objects.equals(personnelNumber, that.personnelNumber);
    }

    public Student(String firstName, String lastName, String telegramName, Long telegramChatId,
        Long telegramUserId,
        boolean isNotificationEnabled, Group group, Long personnelNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.telegramName = telegramName;
        this.telegramChatId = telegramChatId;
        this.telegramUserId = telegramUserId;
        this.isNotificationEnabled = isNotificationEnabled;
        this.group = group;
        this.personnelNumber = personnelNumber;
    }
}
