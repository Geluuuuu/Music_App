import string


class UserDto:
    id : int
    username : string
    password : string
    roles = []

    def __init__ (self, id, username, password, roles):
        self.id = id
        self.username = username
        self.password = password
        self.roles = roles