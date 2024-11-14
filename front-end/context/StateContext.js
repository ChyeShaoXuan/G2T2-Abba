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

  // const fetchUserIdByName = async (name) => {
  //   setLoading(true);
  //   setError(null);
  //   try {
  //     let response;
  //     switch (userType) {
  //       case 'worker':
  //         response = await axios.get(`http://localhost:8080/worker/workerId/${name}`);
  //         break;
  //       case 'admin':
  //         response = await axios.get(`http://localhost:8080/admin/adminId/${name}`);
  //         break;
  //       case 'client':
  //         response = await axios.get(`http://localhost:8080/clients/clientId/${name}`);
  //         break;
  //       default:
  //         throw new Error('Invalid user type');
  //     }

  //     if (response.data) {
  //       setUserId(response.data);
  //       setUsername(name);
  //       localStorage.setItem('userId', response.data);
  //       localStorage.setItem('username', name);
  //       return response.data;
  //     }
  //   } catch (err) {
  //     setError(err.message);
  //     console.error('Error fetching user:', err);
  //   } finally {
  //     setLoading(false);
  //   }
  // };

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
