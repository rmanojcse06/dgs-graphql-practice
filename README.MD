
```bash
mvn dependency:purge-local-repository -DreResolve=false
mvn dependency:tree | grep slf4j
mvn clean
mvn clean package
mvn clean install -U
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=9090

```


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
  addUser(name: "Monkey", phoneNumber: "4234") {
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