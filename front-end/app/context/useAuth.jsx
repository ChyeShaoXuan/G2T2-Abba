"use client";

import { createContext, useContext, useState, useEffect } from 'react';
import axios from 'axios';

const AuthContext = createContext();

export const useAuth = () => {
    return useContext(AuthContext);
};

export const AuthProvider = ({ children }) => {
    // const [user, setUser] = useState(null);

    // useEffect(() => {
    //     // Fetch user data from the backend or local storage
    //     const fetchUser = async () => {
    //         const token = localStorage.getItem('jwtToken');
    //         if (!token) {
    //             console.error('No JWT token found');
    //             return;
    //         }

    //         try {
    //             const res = await axios.get('http://localhost:8080/authentication/user', {
    //                 headers: {
    //                     Authorization: `Bearer ${token}`
    //                 }
    //             });
    //             setUser(res.data);
    //         } catch (error) {
    //             console.error('Failed to fetch user', error);
    //         }
    //     };

    //     fetchUser();
    // }, []);

    const logout = () => {
        // Clear the JWT token from local storage
        localStorage.removeItem('jwtToken');
        router.push('/login')
        
    };

    return (
        <AuthContext.Provider value={{logout }}>
            {children}
        </AuthContext.Provider>
    );
};