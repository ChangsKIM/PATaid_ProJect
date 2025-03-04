import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';

import HomePage from './pages/HomePage';
import LoginPage from './pages/LoginPage';
import MyPage from './pages/MyPage';
import RegisterTypePage from './pages/RegisterTypePage';
import NormalRegisterPage from './pages/NormalRegisterPage';
import CorporateRegisterPage from './pages/CorporateRegisterPage';

import { AuthProvider } from './hooks/AuthContext';

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterTypePage />} />
          <Route path="/register/normal" element={<NormalRegisterPage />} />
          <Route path="/register/corporate" element={<CorporateRegisterPage />} />
          <Route path="/mypage" element={<MyPage />} />
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  );
}

export default App;
