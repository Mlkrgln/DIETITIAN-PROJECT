FROM node:22-alpine AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .
ARG VITE_AUTH_API_URL=http://localhost:8080
ARG VITE_CUSTOMER_API_URL=http://localhost:8081
ARG VITE_APPOINTMENT_API_URL=http://localhost:8082
ARG VITE_DIET_PLAN_API_URL=http://localhost:8083
ENV VITE_AUTH_API_URL=$VITE_AUTH_API_URL
ENV VITE_CUSTOMER_API_URL=$VITE_CUSTOMER_API_URL
ENV VITE_APPOINTMENT_API_URL=$VITE_APPOINTMENT_API_URL
ENV VITE_DIET_PLAN_API_URL=$VITE_DIET_PLAN_API_URL
RUN npm run build

FROM nginx:1.27-alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
