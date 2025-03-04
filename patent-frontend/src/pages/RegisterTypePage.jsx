import React from 'react';
import { useNavigate } from 'react-router-dom';
import Header from '../components/layout/Header';
import Footer from '../components/layout/Footer';
import '../css/RegisterTypePage.css';

function RegisterTypePage() {
  const navigate = useNavigate();

  const handleNormalClick = () => {
    navigate('/register/normal');
  };

  const handleCorporateClick = () => {
    navigate('/register/corporate');
  };

  const handleGoogleClick = () => {
    alert('구글 회원가입(OAuth)은 나중에 구현 예정입니다.');
  };
  const handleKakaoClick = () => {
    alert('카카오 회원가입(OAuth)은 나중에 구현 예정입니다.');
  };

  return (
    <>
      <Header />

      <div className="register-type-container">
        <h2>회원가입 선택</h2>
        <div className="button-group">
          <button onClick={handleNormalClick}>일반 회원가입</button>
          <button onClick={handleCorporateClick}>기업 회원가입</button>
          <button onClick={handleGoogleClick}>구글 회원가입</button>
          <button onClick={handleKakaoClick}>카카오 회원가입</button>
        </div>
      </div>

      <Footer />
    </>
  );
}

export default RegisterTypePage;
