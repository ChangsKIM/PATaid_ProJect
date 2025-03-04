// src/components/layout/Grid.jsx
import React from 'react';
import { Grid } from '@mui/material';
import '../../css/Grid.css'; // Grid 전용 CSS

function MyGrid({ children, ...rest }) {
  return (
    <Grid container spacing={2} className="my-grid" {...rest}>
      {children}
    </Grid>
  );
}

export default MyGrid;
