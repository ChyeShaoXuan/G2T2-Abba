import React, { useState } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';
import styles from './Register.module.css'; // Assuming you are using CSS modules

const REGISTER_URL = 'http://localhost:5000/register';

const Register = () => {
  const [username, setUsername] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [password2, setPassword2] = useState('');
  const [phone, setPhone] = useState('');
  const [isBlank, setIsBlank] = useState(true);
  const [isSubmitted, setIsSubmitted] = useState(false);

  const navigate = useNavigate();

  const register = async (e) => {
    e.preventDefault();
    setIsSubmitted(true);

    if (username === '' || email === '' || password === '' || password2 === '' || phone === '') {
      setIsBlank(true);
      return;
    } else {
      setIsBlank(false);
    }

    try {
      const res = await axios.post(REGISTER_URL, {
        username,
        email,
        password,
        password2,
        phone,
      });

      if (res.status === 200) {
        console.log(res.data);
        // Redirect user to the login page
        navigate('/login');
      }
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <form onSubmit={register}>
      <div className={styles['input-container']}>
        <div className={`${styles['form-container']} ${styles.username}`}>
          <input
            id="username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            placeholder="Username"
          />
          <label htmlFor="username" className={styles['form-label']}>
            Username:
          </label>
        </div>

        <div className={`${styles['form-container']} ${styles.email}`}>
          <input
            type="email"
            id="email"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            placeholder="Email"
          />
          <label htmlFor="email" className={styles['form-label']}>
            Email address:
          </label>
        </div>

        <div className={`${styles['form-container']} ${styles.password}`}>
          <input
            type="password"
            id="password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            placeholder="Password"
          />
          <label htmlFor="password" className={styles['form-label']}>
            Password:
          </label>
        </div>

        <div className={`${styles['form-container']} ${styles.password2}`}>
          <input
            type="password"
            id="password2"
            value={password2}
            onChange={(e) => setPassword2(e.target.value)}
            placeholder="Confirm Password"
          />
          <label htmlFor="password2" className={styles['form-label']}>
            Confirm Password:
          </label>
        </div>

        <div className={`${styles['form-container']} ${styles.phone}`}>
          <input
            type="text"
            id="phone"
            value={phone}
            onChange={(e) => setPhone(e.target.value)}
            placeholder="Phone Number"
          />
          <label htmlFor="phone" className={styles['form-label']}>
            Phone Number:
          </label>
        </div>

        {isBlank && isSubmitted && (
          <div style={{ marginBottom: '15px' }}>Something is missing...</div>
        )}
        {!isBlank && isSubmitted && (
          <div style={{ marginBottom: '15px' }}>Successfully Registered!</div>
        )}

        <div className={styles['login-page']}>
          <button className="btn" id="pdt" onClick={() => navigate('/login')}>
            Login
          </button>
          <button className="btn" id="pdt" type="submit">
            Submit
          </button>
        </div>
      </div>
    </form>
  );
};

export default Register;