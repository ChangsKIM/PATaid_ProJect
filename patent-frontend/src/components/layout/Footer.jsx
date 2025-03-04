// src/components/layout/Footer.jsx
import React from 'react';
import { Box, Typography } from '@mui/material';

function Footer() {
  return (
    <Box 
      component="footer"
      sx={{
        mt: 4,
        py: 2,
        textAlign: 'center',
        backgroundColor: '#f1f1f1',
      }}
    >
      <Typography variant="body2" color="text.secondary">
        © 2025 PATaidProject. All rights reserved.
      </Typography>
    </Box>
  );
}

export default Footer;
