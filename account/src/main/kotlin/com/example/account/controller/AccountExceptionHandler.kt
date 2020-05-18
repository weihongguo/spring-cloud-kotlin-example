package com.example.account.controller

import com.example.security.BaseExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class AccountExceptionHandler : BaseExceptionHandler()