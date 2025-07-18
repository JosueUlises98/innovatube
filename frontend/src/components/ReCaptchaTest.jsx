import React, { useRef } from 'react';
import ReCAPTCHA from 'react-google-recaptcha';

const ReCaptchaTest = () => {
  const recaptchaRef = useRef();

  const testReCaptcha = async () => {
    try {
      console.log('Iniciando prueba de ReCaptcha...');
      
      if (!recaptchaRef.current) {
        console.error('ReCaptcha no está disponible');
        alert('ReCaptcha no está disponible');
        return;
      }

      console.log('Ejecutando ReCaptcha...');
      const token = await recaptchaRef.current.executeAsync();
      
      if (token) {
        console.log('ReCaptcha token obtenido:', token.substring(0, 20) + '...');
        alert('ReCaptcha funcionando correctamente! Token: ' + token.substring(0, 20) + '...');
      } else {
        console.error('No se obtuvo token de ReCaptcha');
        alert('No se obtuvo token de ReCaptcha');
      }
    } catch (error) {
      console.error('Error en ReCaptcha:', error);
      alert('Error en ReCaptcha: ' + error.message);
    }
  };

  return (
    <div className="p-4">
      <h2 className="text-2xl font-bold mb-4">Prueba de ReCaptcha</h2>
      <button 
        onClick={testReCaptcha}
        className="bg-blue-500 text-white px-4 py-2 rounded"
      >
        Probar ReCaptcha
      </button>
      
      <div className="mt-4">
        <ReCAPTCHA
          ref={recaptchaRef}
          sitekey="6LeIxAcTAAAAAJcZVRqyHh71UMIEGNQ_MXjiZKhI"
          size="invisible"
        />
      </div>
    </div>
  );
};

export default ReCaptchaTest; 