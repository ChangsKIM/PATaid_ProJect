// src/components/ProtectedRoute.jsx

import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';

function ProtectedRoute({ children }) {
  const { isAuthenticated } = useAuth();
  const location = useLocation();

  if (!isAuthenticated) {
    // 로그인 안 되어 있으면 /login으로 이동, 이동 전 URL은 state로 저장
    return <Navigate to="/login" state={{ from: location }} replace />;
  }

  // 로그인 상태면 자식 컴포넌트(페이지) 렌더링
  return children;
}

export default ProtectedRoute;
