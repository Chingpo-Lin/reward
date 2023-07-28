
# Chingpo Lin
# Mid-project (Maven springboot project)

## Run RewardApplication file to start springboot application

## Postman testing collection is attached.

### User Controller (/api/v1/users)

- Post: save a user
- Get: get all user
- Get("page"): page user with pageNo, pageSize, SortBy, SortDir
- Get("{id}"): get user by id
- Put("{id}"): update user with id, need requestBody UserDto
- Delete("{id}"): delete user with id

### PurchaseRecord Controller (/api/v1/records)

- Post: save a record
- Get: get all records
- Get("page"): page record with pageNo, pageSize, SortBy, SortDir
- Get("{id}"): get record by id
- Get("search") get records in given date