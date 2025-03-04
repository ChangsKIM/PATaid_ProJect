import React from 'react';
import { Grid } from '@mui/material';

function MyGrid({ children, ...rest }) {
  return (
    <Grid container spacing={2} {...rest}>
      {children}
    </Grid>
  );
}

export default MyGrid;
