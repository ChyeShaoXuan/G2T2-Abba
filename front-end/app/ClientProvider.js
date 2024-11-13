// app/ClientProvider.js
"use client";

import { AuthProvider } from "./context/useAuth";

export default function ClientProvider({ children }) {
  return <AuthProvider>{children}</AuthProvider>;
}
