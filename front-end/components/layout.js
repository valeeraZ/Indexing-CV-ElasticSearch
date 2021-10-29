import * as React from 'react';
import { styled, alpha } from '@mui/material/styles';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import InputBase from '@mui/material/InputBase';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';
import Button from '@mui/material/Button';
import SearchIcon from '@mui/icons-material/Search';
import Container from '@mui/material/Container';
import { useRouter } from 'next/router'
import Dialog from '@mui/material/Dialog';
import DialogActions from '@mui/material/DialogActions';
import DialogContent from '@mui/material/DialogContent';
import DialogContentText from '@mui/material/DialogContentText';
import DialogTitle from '@mui/material/DialogTitle';
import TextField from '@mui/material/TextField';
import Alert from '@mui/material/Alert';
import postCV from '../lib/postCV';
import NextLink from 'next/link'
import Link from '@mui/material/Link'

const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  display: 'block',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha(theme.palette.common.white, 0.15),
  '&:hover': {
    backgroundColor: alpha(theme.palette.common.white, 0.25),
  },
  marginRight: theme.spacing(2),
  marginLeft: 0,
  width: '100%',
  [theme.breakpoints.up('sm')]: {
    marginLeft: theme.spacing(3),
    width: 'auto',
  },
}));

const SearchIconWrapper = styled('div')(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: '100%',
  position: 'absolute',
  pointerEvents: 'none',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: 'inherit',
  '& .MuiInputBase-input': {
    padding: theme.spacing(1, 1, 1, 0),
    // vertical padding + font size from searchIcon
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create('width'),
    width: '100%',
    [theme.breakpoints.up('md')]: {
      width: '20ch',
    },
  },
}));

const Input = styled('input')({
  display: 'none',
  paddingBottom: '1em'
});

export default function Layout({ children }) {
  const router = useRouter()
  const [open, setOpen] = React.useState(false);
  const [file, setFile] = React.useState(undefined);
  const [name, setName] = React.useState(undefined);
  const [error, setError] = React.useState(false);
  const [success, setSuccess] = React.useState(false);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const handleUpload = () => {
    console.log(name)
    console.log(file)
    if (name == undefined || file == undefined) {
      setError(true)
    } else {
      setError(false)
      setSuccess(true)
      // upload
      postCV(file, name);
      window.location.reload(false);
    }
  };

  const getFile = ({ target }) => {
    setFile(target.files[0])
  }
  return (
    <div>
      <Box sx={{ flexGrow: 1 }}>
        <AppBar position="static">
          <Toolbar>
            <NextLink href="/" passHref>
              <Link variant="h6"
                    underline="none"
                    color="#FFFFFF"
                    sx={{ display: { xs: 'none', sm: 'block' } }}>
                Index and search CV

              </Link>
            </NextLink>
            <Box sx={{ flexGrow: 1 }}>
              <Search sx={{ flexGrow: 1 }}>
                <SearchIconWrapper>
                  <SearchIcon />
                </SearchIconWrapper>
                <StyledInputBase
                  placeholder="Search CV with keyword"
                  inputProps={{ 'aria-label': 'Search CV with keyword' }}
                  onKeyPress={
                    (event) => {
                      if (event.key === "Enter") {
                        var keyword = event.target.value
                        if (keyword.length !== 0) {
                          router.push(`/search?keyword=${keyword}`)
                        } else {
                          router.push('/')
                        }
                      }
                    }
                  }
                />
              </Search>
            </Box>
            <Button color="inherit" onClick={handleClickOpen}>Post your CV</Button>
            <Dialog open={open} onClose={handleClose}>
              <DialogTitle>Subscribe</DialogTitle>
              <DialogContent>
                <DialogContentText>
                  To post your CV, please enter your name and upload your CV in .pdf or .docx format.
                </DialogContentText>
                <TextField
                  autoFocus
                  margin="normal"
                  id="name"
                  fullWidth
                  label="Your name"
                  variant="standard"
                  error={error}
                  onChange={(e) => setName(e.target.value)}
                />
                <label htmlFor="contained-button-file" >
                  <Input accept="application/msword, application/pdf" id="contained-button-file" multiple type="file" onChange={getFile} />
                  <Button variant="contained" component="span" color={error ? 'error' : 'success'}>
                    Upload your CV
                  </Button>
                </label>

                <span style={{ marginLeft: '1em' }}>{file ? file.name : ""}</span>
                {(error || success) ?
                  <Alert style={{ marginTop: '1em' }} severity={error ? 'error' : 'success'}>{error ? "Please fill the name and select a file." : "You have successfully uploaded your CV."}</Alert>
                  :
                  null
                }

              </DialogContent>
              <DialogActions>
                <Button onClick={handleClose}>Cancel</Button>
                <Button onClick={handleUpload}>Upload</Button>
              </DialogActions>
            </Dialog>
          </Toolbar>
        </AppBar>
      </Box>

      <main>
        <Container fixed>{children}</Container>
      </main>
    </div>

  );
}
