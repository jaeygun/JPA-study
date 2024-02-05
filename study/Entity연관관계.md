# Entity 연관관계
연관관계 매핑이란, 객체의 참조와 테이블의 외래키를 매핑하는 것을 말한다.

연관관계를 정할 때 아래 3가지를 고려해서 정한다.

1. 방향
2. 연관관계 주인
3. 다중성

<br/>

## 1. 방향
단방향과 양방향 두 가지가 있으며 서로 다른 엔티티 중 한 개의 엔티티 쪽에서만 참조하는 경우를 단방향, 다른 엔티티 두개가 서로를 참조하는 경우를 양방향이라고 볼 수 있다.

데이터베이스에서는 하나의 외래키를 가지고 두 테이블 모두 확인이 가능하기 때문에 사실상 양방향이라고 볼 수 있다.

단방향과 양방향관계는 `@ManyToOne`, `@OneToMany`, `@OneToOne`, `@ManyToMany` 이 네가지의 어노테이션으로 정할 수 있는데 이번 테스트는 가장 많이 사용하는 `@ManyToOne`, `@OneToMany` 이 두가지 어노테이션을 통해서 진행하겠다.

<br/>

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

한 개의 팀에 여러 회원이 속할 수 있기 때문에 N(여러회원):1(한개의팀)의 관계가 되고, 회원 엔티티(N)의 입장에서 봤을 때 `@ManyToOne` 어노테이션으로 단방향 연관관계를 맺을 수 있다.

`@ManyToOne` 어노테이션과 함께 `@JoinColumn` 어노테이션을 함께 사용해서 FK인 컬럼명을 지정할 수 있다.

아래는 단방향 연관관계를 맺은 상태에서 User를 조회하고 그 User가 속한 Team을 같이 조회하는 것을 작성한 테스트 코드이다.

```Java
@SpringBootTest
class RelationTest {

    @Test
    @Transactional
    void relate1() {
        User user = userRepository.findUserByUserUid(1L);
        System.out.println("UserName : " + user.getUserName());
        System.out.println("UserId : " + user.getUserId());
        System.out.println("UserTeam : " + user.getTeam().getTeamName());
    }
}
```
결과 콘솔
```
UserName : 홍길동
UserId : test
UserTeam : 테스트팀
```

<br/>

### 2) 양방향
아래 엔티티는 위의 단방향 상태에서 양방향으로 수정한 상태이다. 양방향 관계를 설정하려면 각 엔티티에서 서로의 존재를 참조할 수 있어야 한다.

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

    @OneToMany(mappedBy = "team")
    private List<User> userList;

}
```

```Java
@Entity
@Table(name = "user")
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userUid;

    @Column(nullable = false, name = "userName")
    private String userName;

    private String userId;

    private String userPw;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teamCode")
    private Team team;

}
```

User 엔티티는 그대로지만 Team 엔티티에는 해당 Team에 속한 User를 받을 수 있는 userList가 추가되었다.

그리고 Team 엔티티 입장에서는 한개의 Team에 여러명의 User가 있을 수 있기 때문에 `@OneToMany` 어노테이션이 추가되어 User와 Team이 서로를 참조할 수 있는 양방향 상태가 되었다.

아래는 Team을 조회해서 해당 팀에 속한 User를 출력하는 테스트 코드이다.
```Java
@SpringBootTest
@Transactional
class RelationTest {

    @Autowired
    TeamRepository teamRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    void relate2() {

        Team team = teamRepository.findByTeamCode(1000);
        System.out.println("TeamName : " + team.getTeamName());
        List<User> userList = team.getUserList();
        for (User user : userList) {
            System.out.println("UserName : " + user.getUserName());
        }
    }

}
```

결과 콘솔
```
TeamName : 테스트팀
UserName : 홍길동
UserName : 홍길동2
```

<br/>

## 2. 연관관계 주인

<br/>

## 3. 다중성