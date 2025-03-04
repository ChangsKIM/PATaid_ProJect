// src/services/api.js
import axios from 'axios';

// ------------------------------
// axios 인스턴스
// ------------------------------
const api = axios.create({
  baseURL: 'http://localhost:8080', // 스프링부트 서버 주소
  withCredentials: false, // 쿠키 기반 인증 시 true
});

// (추가) 요청 인터셉터: localStorage에서 토큰을 읽어 매 요청마다 헤더에 추가
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('token');
  if (token) {
    config.headers['Authorization'] = 'Bearer ' + token;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

// ------------------------------
// 1) 로그인
// ------------------------------
export async function login(username, password) {
  // 백엔드 /api/login (POST) -> { success, message, token, nickname? } 응답
  const response = await api.post('/api/login', { username, password });

  if (!response.data.success) {
    throw new Error(response.data.message || '로그인 실패');
  }
  // 성공이면 { success, message, token, nickname }
  return response.data;
}

// ------------------------------
// 2) 개인 회원가입
// ------------------------------
export async function registerIndividual(userData) {
  // 백엔드 /api/register/individual (POST)
  const response = await api.post('/api/register/individual', userData);
  return response.data; 
}

// ------------------------------
// 3) 기업 회원가입
// ------------------------------
export async function registerCorporate(userData) {
  const response = await api.post('/api/register/corporate', userData);
  return response.data;
}

// ------------------------------
// 4) 아이디 중복확인
// ------------------------------
export async function checkUsername(username) {
  const response = await api.get(`/api/register/check-username?username=${username}`);
  return response.data;
}

// ------------------------------
// 5) 닉네임 중복확인
// ------------------------------
export async function checkNickname(nickname) {
  const response = await api.get(`/api/register/check-nickname?nickname=${nickname}`);
  return response.data;
}

// ------------------------------
// (추가) 토큰 유효성 검증
// ------------------------------
// 백엔드 /api/validate-token (GET) -> { valid: true/false, nickname: '...' }
export async function validateTokenAPI() {
  // 이제 인터셉터가 자동으로 Authorization 헤더를 추가
  const response = await api.get('/api/validate-token');
  // 예: { valid: true, nickname: '홍길동' }
  return response.data;
}

// ----------------------------
