# Entity 연관관계
연관관계 매핑이란, 객체의 참조와 테이블의 외래키를 매핑하는 것을 말한다.

연관관계를 정할 때 아래 3가지를 고려해서 정한다.

1. 방향
2. 연관관계 주인
3. 다중성 (N:1, 1:N, 1:1, N:N)

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

아래는 Team을 조회해서 해당 팀에 속한 User를 출력하면서 User의 팀을 다시 출력해서 양방향으로 연관관계가 맺어졌는지 확인하는 테스트 코드이다.
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
            System.out.println("UserTeam : " + user.getTeam().getTeamName());
        }
    }

}
```

결과 콘솔
```
TeamName : 테스트팀
UserName : 홍길동
UserTeam : 테스트팀
UserName : 홍길동2
UserTeam : 테스트팀
```

<br/>

## 2. 연관관계 주인
위의 양방향 연관관게를 보았을 때 Team엔티티와 User엔티티 모두 FK를 갖는 것 같은 모습이다. 외래키를 갖는 즉 연관관계의 주인이 되는 엔티티를 정해주어야 한다.

연관관계의 주인을 설정 할 때 주인을 설정하는 것이 아니라 자신이 주인이 아님을 설명해야 하는데 그럴 때 사용하는 어노테이션이 `@mappedBy` 이다.

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
위의 코드는 양방향을 설명하면서 작성한 Team 엔티티이다. 1:N 관계에서는 N이 되는 엔티티가 주인이 되며, 위의 예시에서는 User가 주인이기 때문에 Team은 자신이 주인이 아님을 나타내기 위해 `@mappedBy` 를 사용했다. mappedBy의 값은 반대쪽 엔티티에 자신이 참조되어 있는 필드명을 사용해 주면 된다.

아래와 같이 User 엔티티에 team이라는 필드명으로 Team 엔티티가 참조되어 있어서 Team 엔티티에는 `@mappedBy = "team"`이라고 어노테이션이 붙여져 있다.

```Java
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "teamCode")
private Team team;
```

그러면 `@mappedBy`를 붙여서 주인을 지정했을 때와 지정하지 않았을 떄 실행되는 쿼리는 어떤 차이가 있을까?

테스트는 양방향 연관관계를 확인했던 아래 코드를 이용해서 진행하였다.
```Java
@Test
void relate2() {
    Team team = teamRepository.findByTeamCode(1000);
    System.out.println("TeamName : " + team.getTeamName());
    List<User> userList = team.getUserList();
    for (User user : userList) {
        System.out.println("UserName : " + user.getUserName());
        System.out.println("UserTeam : " + user.getTeam().getTeamName());
    }
}
```


#### 1. 엔티티에 `@mappedBy` 를 붙인 경우 실행되는 쿼리
```
Hibernate: 
    select
        t1_0.teamCode,
        t1_0.parentTeamCode,
        t1_0.teamName 
    from
        team t1_0 
    where
        t1_0.teamCode=?

Hibernate: 
    select
        ul1_0.teamCode,
        ul1_0.userUid,
        ul1_0.userId,
        ul1_0.userName,
        ul1_0.userPw 
    from
        user ul1_0 
    where
        ul1_0.teamCode=?
```
위의 쿼리를 보면 정상적으로 Team을 먼저 조회하고 User가 속한 팀을 확인하려고 할 때(user.getTeam().getTeamName()) 또 select 쿼리가 실행된 것을 볼 수 있다.

#### 2. 엔티티에 `@mappedBy` 를 붙이지 않은 경우 실행되는 쿼리
```
Hibernate: 
    select
        t1_0.teamCode,
        t1_0.parentTeamCode,
        t1_0.teamName 
    from
        team t1_0 
    where
        t1_0.teamCode=?

Hibernate: 
    select
        ul1_0.Team_teamCode,
        ul1_1.userUid,
        ul1_1.teamCode,
        ul1_1.userId,
        ul1_1.userName,
        ul1_1.userPw 
    from
        team_userList ul1_0 
    join
        user ul1_1 
            on ul1_1.userUid=ul1_0.userList_userUid 
    where
        ul1_0.Team_teamCode=?

2024-02-06T15:50:48.175+09:00  WARN 26056 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 1146, SQLState: 42S02
2024-02-06T15:50:48.175+09:00 ERROR 26056 --- [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : Table 'jpa_study.team_userlist' doesn't exist
```
`@mappedBy`를 붙이지 않은 경우 Team 조회까지는 정상적으로 되었지만, User가 속한 Team을 조회하려고 쿼리가 한번 더 실행될때 team_userList 라는 테이블과 조인을 하여 조회하려고 하다보니 Table 'jpa_study.team_userlist' doesn't exist 라는 에러가 발생하였다.

`@mappedBy`를 사용하지 않으면 N:1 관계에서 두 테이블을 연결해주는 또 다른 테이블을 생성해서 조인하는 것을 알 수 있다.

JPA 기본 설정 중 필요한 테이블을 create 해주는 설정이 존재하는데, 이번 테스트를 진행할 때는 그 설정은 따로 하지 않아서 테이블이 생성되지 않았다. (그래서 에러가 발생. 만약 해당 설정을 켜두었으면 테이블이 team_userList라는 테이블이 생성되면서 코드가 실행되었을 것이다.)

#### 💡 정리
- 양방향 연관관계를 맺을 때는 `@mappedBy`를 활용해서 연관관계의 주인을 설정하도록 한다.
- N:1, 1:N 관계에서 연관관계의 주인은 N 이다.
- `@mappedBy`값은 주인 엔티티의 참조되는 필드명이다.

<br/>

## 3. 다중성 (N:1, 1:N, 1:1, N:N)
### 1) 다대일(N:1)
제일 처음 설명했던 Team과 User의 관계이다.

### 2) 일대다(1:N)
일대다 관계는 다대일 관계의 반대이기 때문에 똑같은거 아닐까라는 생각을 할 수 있지만, 다대일 관계에서 주인은 다(N) 이고, 일대다 관계에서 주인은 일(1)이다.

아래는 일대다 관계를 한 Team(1), User(N) 엔티티이다.
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

    @OneToMany
    @JoinColumn(name = "userUid")
    private List<User> userList = new ArrayList<>();

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

    private String userName;

    private String userId;

    private String userPw;
}
```
그리고 아래 코드는 user를 생성하고 team까지 생성해보는 테스트 코드이다.
```Java
@Test
void relate2() {
    User user1 = new User();
    user1.setUserId("1");
    user1.setUserPw("1234");
    user1.setUserName("홍길동1");
    userRepository.save(user1);

    User user2 = new User();
    user2.setUserId("2");
    user2.setUserPw("1234");
    user2.setUserName("홍길동2");
    userRepository.save(user2);
    
    Team team = new Team();
    team.setTeamCode(100010);
    team.setParentTeamCode(0);
    team.setTeamName("테스트팀1234");
    team.getUserList().add(user1);
    team.getUserList().add(user2);
    teamRepository.save(team);
    
}
```
1:N 관계에서 1이 주인인 상태로 위의 코드를 실행하면 어떤 쿼리가 실행이 될까?
```
# user 1 저장 (teamCode는 모르기 때문에 null로 저장)
Hibernate: insert into user (userId,userName,userPw) values (?,?,?)

# user 2 저장 (teamCode는 모르기 때문에 null로 저장)
Hibernate: insert into user (userId,userName,userPw) values (?,?,?)

# team save() 전 존재 여부 확인 쿼리
Hibernate: select t1_0.teamCode,t1_0.parentTeamCode,t1_0.teamName from team t1_0 where t1_0.teamCode=?
Hibernate: select u1_0.userUid,u1_0.userId,u1_0.userName,u1_0.userPw from user u1_0 where u1_0.userUid=?
Hibernate: select u1_0.userUid,u1_0.userId,u1_0.userName,u1_0.userPw from user u1_0 where u1_0.userUid=?

# team 저장
Hibernate: insert into team (parentTeamCode,teamName,teamCode) values (?,?,?)

# 저장했던 user1 teamCode update
Hibernate: update user set teamCode=? where userUid=?

# 저장했던 user2 teamCode update
Hibernate: update user set teamCode=? where userUid=?
```
위의 결과에서 User를 저장할 때 teamCode를 모르기 때문에 일단 null로 저장한 다음 Team을 저장한 뒤 다시 User의 teamCode를 업데이트 하는 쿼리를 확인할 수 있다.

1:N 단방향 매핑은 매핑한 객체가 관리하는 외래 키가 다른 테이블에 있다는 단점 때문에 위와 같이 update 쿼리가 한번 더 실행이 되는 모습이다.

성능 문제 때문이기도 있지만 유지보수적 관점으로 봤을 때 위와 같은 연관관계는 좋아 보이진 않는다. 1:N 단방향 매핑보다는 N:1 양방향 매핑을 사용하는 것이 좋다.

만약 N:1 양방향 매핑이라면 아래 쿼리가 실행 됬을 것이다.

```
# team 저장 전 select 후 insert : save() 메소드
Hibernate: select t1_0.teamCode,t1_0.parentTeamCode,t1_0.teamName from team t1_0 where t1_0.teamCode=?
Hibernate: insert into team (parentTeamCode,teamName,teamCode) values (?,?,?)

# user1 저장 전 select 후 insert : save() 메소드
Hibernate: select null,t1_0.parentTeamCode,t1_0.teamName from team t1_0 where t1_0.teamCode=?
Hibernate: insert into user (teamCode,userId,userName,userPw) values (?,?,?,?)

# user2 저장 전 select 후 insert : save() 메소드
Hibernate: select null,t1_0.parentTeamCode,t1_0.teamName from team t1_0 where t1_0.teamCode=?
Hibernate: insert into user (teamCode,userId,userName,userPw) values (?,?,?,?)
```
실행 쿼리가 훨씬 간결해졌다.



### 3) 일대일(1:1)


### 4) 다대다(N:N)
