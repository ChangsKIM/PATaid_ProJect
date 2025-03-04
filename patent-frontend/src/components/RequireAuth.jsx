// src/components/RequireAuth.jsx
import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';

function RequireAuth({ children }) {
  // 예: 토큰이 있는지, 또는 로그인 여부를 확인
  // 여기서는 간단히 localStorage에 token이 있으면 로그인된 상태로 가정
  const token = localStorage.getItem('token');

  const location = useLocation();

  if (!token) {
    // 로그인 안 된 상태
    // 로그인 페이지로 리다이렉트하면서, 이동 전의 위치(location.state) 저장
    return (
      <Navigate
        to="/login"
        replace
        state={{ from: location }}
      />
    );
  }

  // 로그인 상태라면, 자식 컴포넌트(children)를 그대로 렌더링
  return children;
}

export default RequireAuth;
