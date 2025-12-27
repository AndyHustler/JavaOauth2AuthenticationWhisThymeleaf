/*const appInterceptor = axios.create({
    withCredentials: true
});*/

axios.interceptors.response.use(
    
    (response) => {
        console.log('interceptor is work');
        return response;
    },
    async (error) => {
        const originalRequest = error.config;

        if(error.response.status === 401 && !originalRequest._retry) {
            console.log('error.response.status === 401');
            originalRequest._retry = true;

            try {
                await axios.post(REFRESH_URL, {}, {withCredentials: true});
                return axios(originalRequest);
            } catch (refreshError) {
                console.error('Refresn tokef expired. Redirect to login');
                window.location.href = LOGIN_URL;
                return Promise.reject(refreshError);
            }
        }
        window.location.href = LOGIN_URL;
        return Promise.reject(error);
    }
);