'use client'

import React, { createContext, useContext, useState } from 'react';

// Create Context
const StateContext = createContext();

// Create Provider Component
export const StateProvider = ({ children }) => {
  const [userId, setUserId] = useState(null); // Store the ID (WorkerId, ClientId, AdminId)
  const [userType, setUserType] = useState(null); // Store the user type (worker, client, admin)

  return (
    <StateContext.Provider value={{ userId, setUserId, userType, setUserType }}>
      {children}
    </StateContext.Provider>
  );
};

// Custom Hook to Use Context
export const useGlobalState = () => useContext(StateContext);
