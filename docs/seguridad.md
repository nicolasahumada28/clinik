# Seguridad

El acceso usa JWT Bearer sin sesion de servidor. `POST /api/v1/auth/login` recibe `username` y `password` y devuelve `accessToken`.

Las contraseñas iniciales se codifican con BCrypt. El secreto debe configurarse mediante `CLINIK_JWT_SECRET` con al menos 256 bits en Base64. Los roles se convierten en autoridades Spring (`ROLE_ADMIN`, `ROLE_RECEPCIONISTA`, etc.).

Swagger y login son publicos; los recursos de negocio requieren `Authorization: Bearer <token>`. La recuperacion de contraseña, bloqueo por intentos y auditoria son pendientes del roadmap.
