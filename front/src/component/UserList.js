import React from 'react';

const UserList = ({ user }) => (
  <ul>
        {
        user && user.length > 0 && user.map((userObj, index) => (
            <li key={userObj.id}>{userObj.name}</li>
            ))
        }
  </ul>
);

export default UserList;
