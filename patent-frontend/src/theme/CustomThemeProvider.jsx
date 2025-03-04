// src/theme/CustomThemeProvider.jsx
import React from 'react';
import { ThemeProvider, createTheme } from '@mui/material/styles';
import CssBaseline from '@mui/material/CssBaseline';

// 1) MUI 테마 설정
const customTheme = createTheme({
  palette: {
    primary: {
      main: '#1976d2', // 파란색
    },
    secondary: {
      main: '#9c27b0', // 보라색
    },
  },
  typography: {
    fontFamily: 'Roboto, sans-serif',
  },
});

// 2) ThemeProvider 컴포넌트
function CustomThemeProvider({ children }) {
  return (
    <ThemeProvider theme={customTheme}>
      {/* CssBaseline: 기본 MUI 스타일(리셋) */}
      <CssBaseline />
      {children}
    </ThemeProvider>
  );
}

export default CustomThemeProvider;
