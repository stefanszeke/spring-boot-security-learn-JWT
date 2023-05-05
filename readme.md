1. config
2. customer model
3. customer repo
3. user details service custom = Bankbackend user details
    - find by username (email)
    - @Override loadUserByUsername = creates userdetails object, to be used by spring security
    - to injected into the authentication provider
4. customer controller
5. password encoder
    - register Bcrypt password encoder as PasswordEncoder @Bean
6. in controller , hash password before saving to db
7. custom authentication provider (defaulth dao authentication provider)
    - @Bean
    - @Override authenticate
    - @Override supports - authentication manager will call this method to check if this provider can handle the authentication request( is the authentication object of the correct type)
in authentication provider we use userDetailsService to load the user from the db and then compare the password
8. adding rest of the models, controllers and repos 
9. setting up cors on security config,(not on controller level)
10. csrf setup on security config
11. create a filter to send the csrf token to the client with every response

JWT
1. add dependency
2. set session management to stateless in security config
3. JWT generate filter
    - @Override doFilterInternal
    - get the authentication object from the security context
    - make secret key
    - create jwt token
    - add token to the response header
4. in security config add jwt filter after basic auth filter
5. JWT validate filter
    - @Override doFilterInternal
    - get token from the request header
    - validate token
    - get username from token
    - create authentication object
    - set authentication object in security context

    eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJCYW5rYmFja2VuZDIiLCJzdWIiOiJoYXBweUBleGFtcGxlLmNvbSIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSLCBST0xFX0FETUlOIiwiaWF0IjoxNjgzMjg0MjU3LCJleHAiOjE2ODM2Njk2MDB9.xyafOXF59vEBepYXpnTc8nnogahV9_SE6gc6vf6HXac
    
    eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJCYW5rYmFja2VuZDIiLCJzdWIiOiJoYXBweUBleGFtcGxlLmNvbSIsImF1dGhvcml0aWVzIjoiUk9MRV9VU0VSLCBST0xFX0FETUlOIiwiaWF0IjoxNjgzMjg0MjU3LCJleHAiOjE2ODM2Njk2MDB9.xyafOXF59vEBepYXpnTc8nnogahV9_SE6gc6vf6HXac