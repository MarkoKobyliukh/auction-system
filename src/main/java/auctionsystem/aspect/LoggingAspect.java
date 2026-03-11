package auctionsystem.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(LoggingAspect.class);

    private static final String POINTCUT =
            "execution(* auctionsystem.service.impl.AuctionServiceImpl.createAuction(..)) || " +
            "execution(* auctionsystem.service.impl.AuctionServiceImpl.closeAuctionById(..)) || " +
            "execution(* auctionsystem.service.impl.BidServiceImpl.placeBid(..))";

    @Before(POINTCUT)
    public void logBeforeMethod(JoinPoint joinPoint) {

        log.info("Method called: {}", joinPoint.getSignature().getName());

        Object[] args = joinPoint.getArgs();

        if (args.length > 0) {
            log.info("Arguments: {}", args[0]);
        }
    }

    @AfterReturning(pointcut = POINTCUT)
    public void logAfterSuccess(JoinPoint joinPoint) {

        log.info("Method completed successfully: {}", joinPoint.getSignature().getName());
    }

    @AfterThrowing(pointcut = POINTCUT, throwing = "exception")
    public void logAfterException(JoinPoint joinPoint, Exception exception) {

        log.error("Exception in method: {} | message: {}",
                joinPoint.getSignature().getName(),
                exception.getMessage());
    }
}