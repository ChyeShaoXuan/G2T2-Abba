'use client';

import React, { createContext, useContext, useState } from 'react';

// Create Context
const StateContext = createContext();

// Create Provider Component
export const StateProvider = ({ children }) => {
  const [userId, setUserId] = useState(null); // Store the ID (WorkerId, ClientId, AdminId)
  const [userType, setUserType] = useState(null); // Store the user type (worker, client, admin)

  // Logout function to clear state and localStorage
  const logout = () => {
    setUserId(null);
    setUserType(null);
    localStorage.removeItem('userId'); // Clear userId from localStorage if you're persisting it
    localStorage.removeItem('userType'); // Clear userType from localStorage if you're persisting it
  };

  return (
    <StateContext.Provider value={{ userId, setUserId, userType, setUserType, logout }}>
      {children}
    </StateContext.Provider>
  );
};

// Custom Hook to Use Context
export const useGlobalState = () => useContext(StateContext);
