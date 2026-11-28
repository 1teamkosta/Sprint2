package kr.swdl.sprint2.vo;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(generator = "notice_seq", strategy = GenerationType.SEQUENCE)
    private int noticeNumber;
    @Column(nullable = false)
    private String title;
    @Column(nullable = false)
    private String contents;
    @CreationTimestamp
    @Column(nullable = false)
    private Timestamp inDate;
    @Column(nullable = false)
    private int author;
}
