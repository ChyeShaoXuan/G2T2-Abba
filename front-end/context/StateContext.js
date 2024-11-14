
// frontend/context/StateContext.js
'use client';

import React, { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const StateContext = createContext();

export const StateProvider = ({ children }) => {
  const [userId, setUserId] = useState(null);
  const [username, setUsername] = useState(null);
  const [userType, setUserType] = useState(null);
  const [roles, setRoles] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  useEffect(() => {
    const storedUserId = localStorage.getItem('userId');
    const storedUserType = localStorage.getItem('userType');
    const storedUsername = localStorage.getItem('username');
    const storedRoles = localStorage.getItem('roles');

    if (storedUserId) setUserId(storedUserId);
    if (storedUserType) setUserType(storedUserType);
    if (storedUsername) setUsername(storedUsername);
    if (storedRoles) setRoles(JSON.parse(storedRoles));
  }, []);


  const login = (loginResponse) => {
    setUserId(null);  // Reset userId since it will be fetched separately
    setUsername(loginResponse.username);
    setUserType(loginResponse.role);
    setRoles(loginResponse.roles || []);

    localStorage.setItem('username', loginResponse.username);
    localStorage.setItem('userType', loginResponse.role);
    localStorage.setItem('roles', JSON.stringify(loginResponse.roles || []));
    localStorage.setItem('jwtToken', loginResponse.token);
};

  const logout = () => {
    setUserId(null);
    setUserType(null);
    setUsername(null);
    setRoles([]);

    localStorage.removeItem('userId');
    localStorage.removeItem('userType');
    localStorage.removeItem('username');
    localStorage.removeItem('roles');
    localStorage.removeItem('jwtToken');
  };


  return (
    <StateContext.Provider value={{ 
      userId,
      setUserId,
      userType, 
      username,
      roles,
      loading,
      error,
      login,
      logout,
      // fetchUserIdByName,
      isAdmin: roles?.includes('ROLE_ADMIN')
    }}>
      {children}
    </StateContext.Provider>
  );
};

export const useGlobalState = () => useContext(StateContext);
