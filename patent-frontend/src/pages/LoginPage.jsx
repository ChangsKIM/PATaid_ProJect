import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
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

  async function handleSubmit(e) {
    e.preventDefault();
    try {
      const data = await loginAPI(username, password);
      console.log('login response:', data); // 디버깅용
      // data: { success, message, token, nickname }

      if (data.success) {
        login(data.token, data.nickname);
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
        {isAuthenticated && <p>이미 로그인 상태입니다.</p>}
      </div>
      <Footer />
    </>
  );
}

export default LoginPage;
