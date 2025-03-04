// src/services/api.js
import axios from 'axios';

// ------------------------------
// axios 인스턴스
// ------------------------------
const api = axios.create({
  baseURL: 'http://localhost:8080', // 스프링부트 서버 주소
  withCredentials: false, // 쿠키 기반 인증 시 true
});

// ------------------------------
// 1) 로그인
// ------------------------------
export async function login(username, password) {
  // 백엔드 /api/login (POST) -> { success, message, token } 응답
  const response = await api.post('/api/login', { username, password });

  if (!response.data.success) {
    throw new Error(response.data.message || '로그인 실패');
  }
  // 성공이면 { success, message, token }
  return response.data;
}

// ------------------------------
// 2) 개인 회원가입
// ------------------------------
export async function registerIndividual(userData) {
  // 백엔드 /api/register/individual (POST)
  const response = await api.post('/api/register/individual', userData);
  return response.data; // { success, ... } 라고 가정
}

// ------------------------------
// 3) 기업 회원가입
// ------------------------------
export async function registerCorporate(userData) {
  // 백엔드 /api/register/corporate (POST)
  const response = await api.post('/api/register/corporate', userData);
  return response.data;
}

// ------------------------------
// 4) 아이디 중복확인
// ------------------------------
export async function checkUsername(username) {
  // 백엔드 /api/register/check-username (GET) -> { available: true/false }
  const response = await api.get(`/api/register/check-username?username=${username}`);
  return response.data;
}

// ------------------------------
// 5) 닉네임 중복확인
// ------------------------------
export async function checkNickname(nickname) {
  // 백엔드 /api/register/check-nickname (GET) -> { available: true/false }
  const response = await api.get(`/api/register/check-nickname?nickname=${nickname}`);
  return response.data;
}

// ------------------------------
// 필요 시 기본 export도 남겨둠
// ------------------------------
export default api;
