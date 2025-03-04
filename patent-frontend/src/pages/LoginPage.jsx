// src/pages/LoginPage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { login } from '../services/api';

function LoginPage() {
  const navigate = useNavigate();

  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      // login() 호출 -> { success, message, token } 반환
      const data = await login(username, password);

      if (data.token) {
        // 예: localStorage에 토큰 저장
        localStorage.setItem('token', data.token);

        alert('로그인 성공: ' + data.message);
        navigate('/'); // 로그인 후 메인 페이지 등으로 이동
      } else {
        alert('로그인 실패: 토큰 없음');
      }
    } catch (err) {
      alert('로그인 실패: ' + err.message);
    }
  };

  return (
    <div>
      <h2>로그인</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>아이디(username): </label>
          <input
            type="text"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
          />
        </div>
        <div>
          <label>비밀번호(password): </label>
          <input
            type="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
          />
        </div>
        <button type="submit">로그인</button>
      </form>
    </div>
  );
}

export default LoginPage;
