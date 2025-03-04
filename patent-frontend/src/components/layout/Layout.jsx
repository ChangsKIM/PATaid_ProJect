// src/components/layout/Layout.jsx
import React from 'react';
import Header from './Header';
import Footer from './Footer';
import { Box } from '@mui/material';

function Layout({ children }) {
  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh' }}>
      {/* 상단바 */}
      <Header />

      {/* 메인 컨텐츠 영역 */}
      <Box component="main" sx={{ flex: '1 0 auto', p: 2 }}>
        {children}
      </Box>

      {/* 하단바 */}
      <Footer />
    </Box>
  );
}

export default Layout;
