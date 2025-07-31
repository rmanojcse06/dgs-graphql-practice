
```bash
mvn dependency:purge-local-repository -DreResolve=false
mvn dependency:tree | grep slf4j
mvn clean
mvn clean package
mvn clean install -U
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090

```

### Schema file:
- [schema.graphqls](src/main/resources/schema/user/schema.graphqls)

### Models:
- [UserModel](src/main/java/edu/man/dgs/model/UserModel.java)
- [Post](src/main/java/edu/man/dgs/model/Post.java)

### Scalar: dateTime
- [DateTimeScalar](src/main/java/edu/man/dgs/utils/DateTimeScalar.java)

### Fetchers: query and mutation
- [DataFetcher](src/main/java/edu/man/dgs/query/DataFetcher.java)

### Directives: upper / lower
- [DirectiveConfigurer](src/main/java/edu/man/dgs/utils/DirectiveConfigurer.java)
- [UpperDirective](src/main/java/edu/man/dgs/utils/UpperDirective.java)

### Resolvers:
- [UserResolver](src/main/java/edu/man/dgs/utils/UserResolver.java)


Open igraphql in browser: http://localhost:9090/graphiql

GET ALL USERS
```graphql
query {
  users {
   name 
   id
   role 
    
    ... on AdminUser {
      seed
    }
    ...on GuestUser {
      phoneNumber
    }
  }
}
```

ADDING A USER : OPERATION

```graphql
mutation {
    add_user(name: "Monkey", phoneNumber: "4234") {
        id
        name
        email
        ...on GuestUser {
          phoneNumber
        }
  }
}
```

GET A USER BY ID

```graphql
query {
    user(id :"21"){
        id
        name
        email
        role
        ... on AdminUser {
            seed
        }
        ...on GuestUser {
            phoneNumber
        }
    }
}
```

GET ALL USERS WITH DIFFERENT ROLES: 

```graphql

query {
    
    all_guests: get_guests {
    	id
    	name
    	email
      phoneNumber
      role
    },
    all_admins: get_admins {
      name
      role
      seed
      phoneNumber
      email
    },
  	first_user:user(id :"1"){
        id
        name
        email
        role
        ... on AdminUser {
            seed
        }
    },
}

```

POST A MESSAGE

```graphql

mutation {
  add_post(
    title: "GraphQL with DGS"
    content: "Netflix DGS is awesome!"
    userInput: {
      id: "1"
      name: "Manoj"
    }
  ) {
    id
    title
    createdAt
    addedBy {
      name
      role
      email
    }
  }
}

```

QUERY A POST

```graphql

query {
    posts_before(dateTime :"2025-07-31 21:14:49") {
        id
        title
        addedBy {
            name
            role
            email
        }
    }
}
```

```graphql

query {
    posts_before(dateTime :"2025-07-31 21:14:49") {
        id
        title
        addedBy {
            name
            role
            email
        }
    }
}
```


We can also use codegen to generate model:

```xml
            <plugin>
                <groupId>io.github.deweyjose</groupId>
                <artifactId>graphqlcodegen-maven-plugin</artifactId>
                <version>3.5.0</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>generate</goal>
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <schemaPaths>
                        <param>src/main/resources/schema/user/schema.graphqls</param>
                    </schemaPaths>
                    <packageName>edu.man.dgs.model</packageName>
                </configuration>
            </plugin>


```