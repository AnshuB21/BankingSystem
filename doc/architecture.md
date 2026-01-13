1. The securityConfig class we are going to override the default security method that has been defined springboot 
2. The AppConfig is the "Connect to database for authentication" where we have 
initialized the userdetailService , Dao authentication and our password encoder.
3. In jwtAuthentication filter we are doing the jwt token authentication, if the header
has the bearer token with it.