package com.a1s.discovery;

import java.sql.Statement;

import com.zaxxer.hikari.util.ConcurrentBag.IConcurrentBagEntry;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.nativex.hint.NativeHint;
import org.springframework.nativex.hint.TypeAccess;
import org.springframework.nativex.hint.TypeHint;

@SpringBootApplication
@NativeHint(types = { @TypeHint(types = ImplicitNamingStrategyJpaCompliantImpl.class, access = TypeAccess.PUBLIC_CONSTRUCTORS), @TypeHint(types = { IConcurrentBagEntry[].class, IConcurrentBagEntry.class, Statement.class, Statement[].class }) })
public class McDpApplication {

	public static void main(String[] args) {
		SpringApplication.run(McDpApplication.class, args);
	}

}
