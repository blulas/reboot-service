# Derived from official PostgreSQL image
# Official PostgreSQL image uses Alpine Linux v3.14
FROM postgres:10.17-alpine

# Update packages
RUN apk -U upgrade

# Seed the database
COPY ./iim-schema.sql /docker-entrypoint-initdb.d/