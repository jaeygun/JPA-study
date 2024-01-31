package com.study.jpa.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/*
 * @Entity > JPA entity 클래스임을 나타낸다. JPA는 데이터베이스 테이블 간의 매핑을 위한 entity 클래스를 사용한다.
 * @Table > JPA entity 클래스와 데이터베이스 테이블 간의 매핑을 명시적으로 나타낸다. (name = "테이블 명")
 */
@Entity
@Table(name = "user")
@Setter
@Getter
public class User {

    /*
     * @Id > JPA entity 클래스의 기본 키를 나타낸다.
     * @GeneratedValue > entity 클래스의 기본 키를 어떻게 생성할지 지정하는데 사용한다.
     *   일반적으로 사용되는 옵션은 크게 아래 두 가지가 있다.
     *   GenerationType.IDENTITY > 데이터베이스에서 자동으로 증가하는 기본 키를 사용한다는 의미 (ex. Mysql, MariaDB, PostgreSQL..)
     *   GenerationType.SEQUENCE > 데이터베이스 시퀀스를 사용하여 기본 키를 생성 (ex oracle..)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userUid;

    /*
     * @Column > entity필드를 테이블 컬럼에 매핑할때 사용한다.
     *  주로 name과 nullable이 사용되지만, 필드명과 테이블 컬럼이 일치하다면 사용하지 않아도 무방하다.
     */
    @Column(nullable = false, name = "userName")
    private String userName;

    private String userId;

    private String userPw;

    private int teamCode;
}
