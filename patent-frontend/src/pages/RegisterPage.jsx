// src/pages/RegisterPage.jsx
import React, { useState } from 'react';
import {
  registerIndividual,
  registerCorporate,
  checkUsername,
  checkNickname,
} from '../services/api';
import { useNavigate } from 'react-router-dom';

import '../assets/css/RegisterPage.css';

function RegisterPage() {
  // step: 1 = 회원가입 타입 선택 페이지, 2 = 회원가입 입력 페이지
  const [step, setStep] = useState(1);

  // 어떤 타입의 회원가입인지 state로 관리
  // (google, kakao, normal, corporate)
  const [memberType, setMemberType] = useState('');

  // 폼 입력값
  const [form, setForm] = useState({
    username: '',
    nickname: '',
    password: '',
    confirmPassword: '',
    email: '',
    // 기업 회원가입 시 필요한 정보
    companyName: '',
    address: '',
    contact: '',
    businessRegistrationNumber: '',
  });

  // 중복확인 결과 메세지
  const [usernameCheckMsg, setUsernameCheckMsg] = useState('');
  const [nicknameCheckMsg, setNicknameCheckMsg] = useState('');

  // 회원가입 완료 후 메인 페이지(HomePage.jsx)로 이동
  const navigate = useNavigate();

  // 입력값 변경 핸들러
  const handleChange = (e) => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
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

    // (공통) 비밀번호 유효성 검사: 6~16자, 숫자/특수문자 최소 1개씩
    const passwordRegex = /^(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{6,16})/;
    if (!passwordRegex.test(form.password)) {
      alert('비밀번호는 6~16자이며, 숫자와 특수문자를 최소 1개 이상 포함해야 합니다.');
      return;
    }

    // 비밀번호 재확인
    if (form.password !== form.confirmPassword) {
      alert('비밀번호와 비밀번호 확인이 일치하지 않습니다.');
      return;
    }

    // 이메일 유효성 검사(간단 예시)
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(form.email)) {
      alert('유효한 이메일 주소를 입력해주세요.');
      return;
    }

    try {
      let result;
      if (memberType === 'normal') {
        // 일반 회원가입
        // 기존 registerIndividual API를 사용
        result = await registerIndividual({
          username: form.username,
          nickname: form.nickname,
          password: form.password,
          email: form.email,
        });
      } else if (memberType === 'corporate') {
        // 기업 회원가입
        result = await registerCorporate({
          companyName: form.companyName,
          address: form.address,
          contact: form.contact,
          businessRegistrationNumber: form.businessRegistrationNumber,
          password: form.password,
          email: form.email,
        });
      } else {
        // 구글/카카오의 경우 별도 로직이 필요
        // 여기서는 간단히 예시 메시지 처리
        alert(`${memberType} 회원가입은 별도의 OAuth 과정을 통해 진행됩니다.`);
        return;
      }

      console.log('가입 성공:', result);
      alert('가입 성공!');
      // 가입 후 메인 페이지로 이동 (HomePage.jsx가 /home에 매핑되어 있다고 가정)
      navigate('/');
    } catch (err) {
      console.error('가입 실패:', err);
      alert('가입 실패: ' + err.message);
    }
  };

  // step=1: 회원가입 타입 선택 화면
  const renderRegisterType = () => {
    return (
      <div>
        <h2>회원가입 타입 선택</h2>
        <div>
          <label>
            <input
              type="radio"
              name="memberType"
              value="google"
              onChange={(e) => setMemberType(e.target.value)}
            />
            구글 회원가입
          </label>
        </div>
        <div>
          <label>
            <input
              type="radio"
              name="memberType"
              value="kakao"
              onChange={(e) => setMemberType(e.target.value)}
            />
            카카오 회원가입
          </label>
        </div>
        <div>
          <label>
            <input
              type="radio"
              name="memberType"
              value="normal"
              onChange={(e) => setMemberType(e.target.value)}
            />
            일반 회원가입
          </label>
        </div>
        <div>
          <label>
            <input
              type="radio"
              name="memberType"
              value="corporate"
              onChange={(e) => setMemberType(e.target.value)}
            />
            기업 회원가입
          </label>
        </div>
        <button
          type="button"
          onClick={() => {
            if (!memberType) {
              alert('회원가입 타입을 선택해주세요.');
              return;
            }
            setStep(2);
          }}
        >
          다음
        </button>
      </div>
    );
  };

  // step=2: 회원가입 입력 폼 화면
  const renderRegisterForm = () => {
    // (1) 구글/카카오인 경우 안내 문구만 표시(실제 OAuth 로직은 별도 구성)
    if (memberType === 'google') {
      return (
        <div>
          <h2>구글 회원가입</h2>
          <p>구글 OAuth로 진행합니다.</p>
          <button onClick={() => alert('구글 OAuth 로직 연동 필요')}>구글 회원가입 진행</button>
        </div>
      );
    }
    if (memberType === 'kakao') {
      return (
        <div>
          <h2>카카오 회원가입</h2>
          <p>카카오 OAuth로 진행합니다.</p>
          <button onClick={() => alert('카카오 OAuth 로직 연동 필요')}>카카오 회원가입 진행</button>
        </div>
      );
    }

    // (2) 일반 or 기업 회원가입 폼
    return (
      <div>
        <h2>{memberType === 'normal' ? '일반 회원가입' : '기업 회원가입'}</h2>
        <form onSubmit={handleSubmit}>
          {memberType === 'normal' && (
            <>
              {/* 아이디 / 닉네임 / 중복체크 */}
              <div>
                <label>아이디(username): </label>
                <input
                  type="text"
                  name="username"
                  value={form.username}
                  onChange={handleChange}
                />
                <button type="button" onClick={handleCheckUsername}>
                  아이디 중복확인
                </button>
                <div>{usernameCheckMsg}</div>
              </div>

              <div>
                <label>닉네임(nickname): </label>
                <input
                  type="text"
                  name="nickname"
                  value={form.nickname}
                  onChange={handleChange}
                />
                <button type="button" onClick={handleCheckNickname}>
                  닉네임 중복확인
                </button>
                <div>{nicknameCheckMsg}</div>
              </div>
            </>
          )}

          {memberType === 'corporate' && (
            <>
              {/* 기업 정보 */}
              <div>
                <label>회사명(companyName): </label>
                <input
                  type="text"
                  name="companyName"
                  value={form.companyName}
                  onChange={handleChange}
                />
              </div>
              <div>
                <label>주소(address): </label>
                <input
                  type="text"
                  name="address"
                  value={form.address}
                  onChange={handleChange}
                />
              </div>
              <div>
                <label>연락처(contact): </label>
                <input
                  type="text"
                  name="contact"
                  value={form.contact}
                  onChange={handleChange}
                />
              </div>
              <div>
                <label>사업자등록번호(businessRegistrationNumber): </label>
                <input
                  type="text"
                  name="businessRegistrationNumber"
                  value={form.businessRegistrationNumber}
                  onChange={handleChange}
                />
              </div>
            </>
          )}

          {/* 비밀번호, 비밀번호 확인, 이메일 (공통) */}
          <div>
            <label>비밀번호(password): </label>
            <input
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
            />
          </div>

          <div>
            <label>비밀번호 재입력(confirmPassword): </label>
            <input
              type="password"
              name="confirmPassword"
              value={form.confirmPassword}
              onChange={handleChange}
            />
          </div>

          <div>
            <label>이메일(email): </label>
            <input
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
            />
          </div>

          <button type="submit">회원가입</button>
        </form>
      </div>
    );
  };

  return (
    <div className="register-page">
      {step === 1 && renderRegisterType()}
      {step === 2 && renderRegisterForm()}
  
      {step === 2 && (
        <button type="button" onClick={() => setStep(1)}>
          이전
        </button>
      )}
    </div>
  );  
}

export default RegisterPage;
