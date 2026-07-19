We are splitting the Firebase authentication into two separate endpoints. 

The backend will be the single source of truth for generating and tracking user stats. Here are the final payloads:

### 1. Registration (POST auth/register)
We send the token along with the user's input. We **do not** send the stats object — the backend will securely initialize those to 0 in the database.

**Mobile Sends:**
```json
{
  "user_token": "jwtlfiqxJdlaszz",
  "name": "Abdullh Mohamed",
  "avatar_url": ""
}
```

```json
**Backend Returns:**
{
  "user_id": "usr_firebase_99812",
  "email": "abdullh@example.com",
  "name": "Abdullh Mohamed",
  "avatar_url": "",
  "stats": {
    "total_study_hours": 0,
    "completed_tasks_count": 0,
    "current_streak_days": 0
  }
}
```

### 2. Login (POST auth/login)
For returning users, we only send the token. The backend verifies it, looks up the user, and returns their current progress.

**Mobile Sends:**
```json
{
  "user_token": "jwtlfiqxJdlaszz"
}
```

**Backend Returns:**
```json
{
  "user_id": "usr_firebase_99812",
  "email": "abdullh@example.com",
  "name": "Abdullh Mohamed",
  "avatar_url": "",
  "stats": {
    "total_study_hours": 145,
    "completed_tasks_count": 382,
    "current_streak_days": 14
  }
}
```