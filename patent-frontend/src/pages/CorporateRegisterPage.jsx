// src/pages/CorporateRegisterPage.jsx
import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { registerCorporate } from '../services/api';
import '../css/CorporateRegisterPage.css'; // CSS 파일 임포트
import Header from '../components/layout/Header';
import Footer from '../components/layout/Footer';

function CorporateRegisterPage() {
  const navigate = useNavigate();

  // 폼 입력값
  const [form, setForm] = useState({
    companyName: '',
    address: '',
    contact: '',
    businessRegistrationNumber: '',
    password: '',
    confirmPassword: '',
    email: '',
  });

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
      const result = await registerCorporate({
        companyName: form.companyName,
        address: form.address,
        contact: form.contact,
        businessRegistrationNumber: form.businessRegistrationNumber,
        password: form.password,
        email: form.email,
      });

      console.log('기업 회원가입 성공:', result);
      alert('기업 회원가입 성공!');
      navigate('/');
    } catch (err) {
      console.error('기업 회원가입 실패:', err);
      alert('기업 회원가입 실패: ' + err.message);
    }
  };

  return (
    <>
    <Header/>
    <div className="corporate-register-container">
      <h2>기업 회원가입</h2>
      <form onSubmit={handleSubmit} className="register-form">
        <div className="form-group">
          <label>회사명 : </label>
          <input
            type="text"
            name="companyName"
            value={form.companyName}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>주소 : </label>
          <input
            type="text"
            name="address"
            value={form.address}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>전화번호 : </label>
          <input
            type="text"
            name="contact"
            value={form.contact}
            onChange={handleChange}
          />
        </div>

        <div className="form-group">
          <label>사업자등록번호 : </label>
          <input
            type="text"
            name="businessRegistrationNumber"
            value={form.businessRegistrationNumber}
            onChange={handleChange}
          />
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
          {form.confirmPassword && (
            passwordMatch === true ? (
              <div className="match-msg match-true">비밀번호가 일치합니다.</div>
            ) : passwordMatch === false ? (
              <div className="match-msg match-false">비밀번호가 일치하지 않습니다.</div>
            ) : null
          )}
        </div>

        <div className="form-group">
          <label>이메일(email) : </label>
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

export default CorporateRegisterPage;
