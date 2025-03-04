// 예: src/components/common/Button.jsx (직접 래핑해서 쓸 수도 있음)
import React from 'react';
import Button from '@mui/material/Button';

function MyButton({ children, ...rest }) {
  return (
    <Button variant="contained" color="primary" {...rest}>
      {children}
    </Button>
  );
}

export default MyButton;
