// src/pages/NormalRegisterPage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerIndividual, checkUsername, checkNickname } from '../services/api';
import '../css/NormalRegisterPage.css'; // CSS 파일 임포트
import Header from '../components/layout/Header';
import Footer from '../components/layout/Footer';

function NormalRegisterPage() {
  const navigate = useNavigate();

  // 폼 입력값
  const [form, setForm] = useState({
    username: '',
    nickname: '',
    password: '',
    confirmPassword: '',
    email: '',
  });

  // 중복확인 결과 메세지
  const [usernameCheckMsg, setUsernameCheckMsg] = useState('');
  const [nicknameCheckMsg, setNicknameCheckMsg] = useState('');

  // 비밀번호 일치 상태: null | true | false
  const [passwordMatch, setPasswordMatch] = useState(null);

  // 입력값 변경 핸들러
  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));

    // 비밀번호/비밀번호 재입력 비교 로직
    if (name === 'password') {
      if (form.confirmPassword) {
        setPasswordMatch(value === form.confirmPassword);
      } else {
        setPasswordMatch(null);
      }
    } else if (name === 'confirmPassword') {
      if (value) {
        setPasswordMatch(value === form.password);
      } else {
        setPasswordMatch(null);
      }
    }
  };

  // 아이디 중복확인
  const handleCheckUsername = async () => {
    if (!form.username) {
      setUsernameCheckMsg('아이디를 입력해주세요.');
      return;
    }
    try {
      const res = await checkUsername(form.username);
      if (res.available) {
        setUsernameCheckMsg('사용 가능한 아이디입니다.');
      } else {
        setUsernameCheckMsg('이미 사용 중인 아이디입니다.');
      }
    } catch (err) {
      setUsernameCheckMsg('중복확인 오류: ' + err.message);
    }
  };

  // 닉네임 중복확인
  const handleCheckNickname = async () => {
    if (!form.nickname) {
      setNicknameCheckMsg('닉네임을 입력해주세요.');
      return;
    }
    try {
      const res = await checkNickname(form.nickname);
      if (res.available) {
        setNicknameCheckMsg('사용 가능한 닉네임입니다.');
      } else {
        setNicknameCheckMsg('이미 사용 중인 닉네임입니다.');
      }
    } catch (err) {
      setNicknameCheckMsg('중복확인 오류: ' + err.message);
    }
  };

  // 회원가입 버튼 클릭
  const handleSubmit = async (e) => {
    e.preventDefault();

    // 비밀번호 일치 최종 확인
    if (passwordMatch !== true) {
      alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
      return;
    }

    // 비밀번호 유효성 검사
    const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{6,16})/;
    if (!passwordRegex.test(form.password)) {
      alert('비밀번호는 6~16자이며, 숫자와 특수문자를 최소 1개 이상 포함해야 합니다.');
      return;
    }

    // 이메일 유효성 검사
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email)) {
      alert('유효한 이메일 주소를 입력해주세요.');
      return;
    }

    try {
      const result = await registerIndividual({
        username: form.username,
        nickname: form.nickname,
        password: form.password,
        email: form.email,
      });

      console.log('가입 성공:', result);
      alert('가입 성공!');
      navigate('/');
    } catch (err) {
      console.error('가입 실패:', err);
      alert('가입 실패: ' + err.message);
    }
  };

  return (
    <>
    <Header/>
    <div className="normal-register-container">
      <h2>일반 회원가입</h2>
      <form onSubmit={handleSubmit} className="register-form">
        <div className="form-group">
          <label>아이디 : </label>
          <div className="input-group">
            <input
              type="text"
              name="username"
              value={form.username}
              onChange={handleChange}
            />
            <button type="button" onClick={handleCheckUsername} className="btn-check">
              아이디 중복확인
            </button>
          </div>
          <div className="msg-username">{usernameCheckMsg}</div>
        </div>

        <div className="form-group">
          <label>닉네임 : </label>
          <div className="input-group">
            <input
              type="text"
              name="nickname"
              value={form.nickname}
              onChange={handleChange}
            />
            <button type="button" onClick={handleCheckNickname} className="btn-check">
              닉네임 중복확인
            </button>
          </div>
          <div className="msg-nickname">{nicknameCheckMsg}</div>
        </div>

        <div className="form-group">
          <label>비밀번호 : </label>
          <input
            type="password"
            name="password"
            value={form.password}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>비밀번호 재입력 : </label>
          <input
            type="password"
            name="confirmPassword"
            value={form.confirmPassword}
            onChange={handleChange}
          />
          {/* 비밀번호 일치/불일치 메시지 */}
          {form.confirmPassword && (
            passwordMatch === true ? (
              <div className="match-msg match-true">비밀번호가 일치합니다.</div>
            ) : passwordMatch === false ? (
              <div className="match-msg match-false">비밀번호가 일치하지 않습니다.</div>
            ) : null
          )}
        </div>

        <div className="form-group">
          <label>이메일(email): </label>
          <input
            type="email"
            name="email"
            value={form.email}
            onChange={handleChange}
          />
        </div>

        <button type="submit" className="btn-submit">회원가입</button>
      </form>

      <button
        type="button"
        onClick={() => navigate('/register')}
        className="btn-back"
      >
        회원가입 선택으로 돌아가기
      </button>
    </div>
    <Footer/>
    </>
  );
}

export default NormalRegisterPage;
