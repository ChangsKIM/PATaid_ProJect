// src/pages/LoginPage.jsx
import React, { useState } from 'react';
import { useNavigate, Navigate } from 'react-router-dom';
import { login as loginAPI } from '../services/api'; 
import { useAuth } from '../hooks/AuthContext';
import Header from '../components/layout/Header';
import Footer from '../components/layout/Footer';
import '../css/LoginPage.css';

function LoginPage() {
  const navigate = useNavigate();
  const { isAuthenticated, login } = useAuth();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  // (A) 이미 로그인된 사용자가 /login 페이지에 접근하면
  //     메인(/) 등 다른 경로로 리다이렉트
  if (isAuthenticated) {
    return <Navigate to="/" replace />;
    // 혹은 "return null;" 하고 '이미 로그인' 알림 표시 가능
  }

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      // 백엔드 /api/login
      const data = await loginAPI(username, password);
      // data: { success, message, token, nickname, role }
      if (data.success) {
        // AuthContext.login(token, nickname, role)
        login(data.token, data.nickname, data.role);
        alert('로그인 성공: ' + data.message);
        navigate('/');
      } else {
        alert('로그인 실패: ' + data.message);
      }
    } catch (err) {
      alert('로그인 오류: ' + err.message);
    }
  }

  return (
    <>
      <Header />
      <div className="login-container">
        <h2>로그인 페이지</h2>
        <form onSubmit={handleSubmit} className="login-form">
          <div className="form-group">
            <label>아이디: </label>
            <input
              type="text"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
            />
          </div>
          <div className="form-group">
            <label>비밀번호: </label>
            <input
              type="password"
              value={password}
              onChange={(e) => setPassword(e.target.value)}
            />
          </div>
          <button type="submit" className="btn-login">로그인</button>
        </form>
      </div>
      <Footer />
    </>
  );
}

export default LoginPage;
