package site.lets_onion.lets_onionApp.domain.friendship;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(uniqueConstraints = @UniqueConstraint(
    columnNames = {"from_member_id", "to_member_id"}
))
public class Friendship {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "friendship_id")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "from_member_id")
  private Member fromMember;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "to_member_id")
  private Member toMember;

  @Enumerated(EnumType.STRING)
  private FriendshipStatus status;

  @CreatedDate
  private LocalDateTime createdAt;
  private LocalDateTime acceptedAt;

  @Builder
  public Friendship(Long id, Member fromMember, Member toMember) {
    this.id = id;
    this.fromMember = fromMember;
    this.toMember = toMember;
    status = FriendshipStatus.PENDING;
  }

  public void updateStatus(FriendshipStatus newStatus) {
    this.status = newStatus;
    if (newStatus.equals(FriendshipStatus.ACCEPTED)) {
      acceptedAt = LocalDateTime.now();
    }
  }
}
