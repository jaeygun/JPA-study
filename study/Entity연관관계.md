# Entity 연관관계
연관관계 매핑이란, 객체의 참조와 테이블의 외래키를 매핑하는 것을 말한다.

연관관계를 정할 때 아래 3가지를 고려해서 정한다.

1. 방향
2. 연관관계 주인
3. 다중성

## 1. 방향
단방향과 양방향 두 가지가 있으며 서로 다른 엔티티 중 한 개의 엔티티 쪽에서만 참조하는 경우를 단방향, 다른 엔티티 두개가 서로를 참조하는 경우를 양방향이라고 볼 수 있다.

데이터베이스에서는 하나의 외래키를 가지고 두 테이블 모두 확인이 가능하기 때문에 사실상 양방향이라고 볼 수 있다.

### 1) 단방향
아래는 회원을 조회하면 그 회원이 속한 팀을 볼 수 있게 한 단방향 예시이다.
```java
@Entity
@Table(name = "user")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userUid;

    private String userName;

    private String userId;

    private String userPw;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamCode")
    private Team team;
}
```

```Java
@Entity
@Table(name = "team")
@Getter
@Setter
public class Team {

    @Id
    private int teamCode;

    private int parentTeamCode;

    private String teamName;
}
```


