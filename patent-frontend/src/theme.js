// src/theme.js
import { createTheme } from '@mui/material/styles';

const theme = createTheme({
  palette: {
    primary: {
      main: '#1976d2', // 파랑
    },
    secondary: {
      main: '#9c27b0', // 보라
    },
  },
  typography: {
    fontFamily: 'Roboto, sans-serif',
  },
  // etc...
});

export default theme;
